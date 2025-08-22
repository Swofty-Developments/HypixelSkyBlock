package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronGetStateProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronInputProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronNextRoundProtocolObject;
import net.swofty.commons.protocol.objects.experimentation.ChronomatronFinishProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUIChronomatronPlay extends HypixelInventoryGUI implements RefreshingGUI {

    private final String tier;
    private final int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
    // Proper 3x3 grid centered in a 6-row GUI
    private final int[] gridSlots = {21, 22, 23, 30, 31, 32, 39, 40, 41};

    // Base colors by column (left=red, middle=blue, right=green) using solid glass blocks
    private final Material[] colorPalette = new Material[] {
            Material.RED_STAINED_GLASS,     // 0 (col 0)
            Material.BLUE_STAINED_GLASS,    // 1 (col 1)
            Material.LIME_STAINED_GLASS     // 2 (col 2)
    };

    private volatile List<Integer> sequence = new ArrayList<>();
    private volatile boolean revealing = true;
    private volatile int revealIndex = 0;
    private volatile int tickCounter = 0;
    private final int ticksPerReveal = 10; // half second-ish
    private volatile int lastHighlight = -1;
    private volatile boolean acceptingInput = false;
    private final List<Integer> currentInput = Collections.synchronizedList(new ArrayList<>());

    // Timer (bookshelf) – counts down during input
    private final int timerSlot = 4;
    private volatile int timerSecondsRemaining = 0;
    private int timerCycleCounter = 0; // counts refresh cycles; 10 cycles ~= 1 second at refreshRate()=2

    public GUIChronomatronPlay(String tier) {
        super("Chronomatron (" + tier + ")", InventoryType.CHEST_6_ROW);
        this.tier = tier;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        try {
            fill(Material.BLACK_STAINED_GLASS_PANE, " ");

            for (int i : borderSlots) {
                set(new GUIItem(i) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        return ItemStackCreator.getStack(" ", Material.PURPLE_STAINED_GLASS_PANE, 1);
                    }
                });
            }
            // Initialize 3x3 grid with base colors
            for (int i = 0; i < gridSlots.length; i++) {
                set(gridSlots[i], ItemStackCreator.getStack(" ", baseColorForIndex(i), 1));
            }
            // Initialize timer display (bookshelf)
            timerSecondsRemaining = secondsForTier();
            set(timerSlot, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
        } catch (Exception ex) {
            e.player().sendMessage("§cFailed to open Chronomatron: " + ex.getMessage());
        }

        set(GUIClickableItem.getGoBackItem(49, new GUIChronomatron()));

        // Grid click handlers
        for (int i = 0; i < gridSlots.length; i++) {
            final int index = i;
            set(new GUIClickableItem(gridSlots[i]) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    if (!acceptingInput) return;
                    currentInput.add(index);

                    // Play a subtle pling sound per click
                    float clickPitch = 0.9f + (index * 0.03f);
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, clickPitch));

                    // Brief highlight feedback on click
                    int clickedSlot = gridSlots[index];
                    set(clickedSlot, ItemStackCreator.enchant(ItemStackCreator.getStack(" ", baseColorForIndex(index), 1)));

                    ProxyService service = new ProxyService(ServiceType.EXPERIMENTATION);
                    ChronomatronInputProtocolObject.InputMessage req = new ChronomatronInputProtocolObject.InputMessage(player.getUuid(), new ArrayList<>(currentInput));
                    service.handleRequest(req).thenAccept(response -> {
                        ChronomatronInputProtocolObject.InputResponse res = (ChronomatronInputProtocolObject.InputResponse) response;
                        if (!res.success) {
                            player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 1.0f, 0.5f));
                            player.sendMessage("§cIncorrect! Chain broken.");
                            // On failure, finish session and award
                            ProxyService finSvc = new ProxyService(ServiceType.EXPERIMENTATION);
                            finSvc.handleRequest(new ChronomatronFinishProtocolObject.FinishMessage(player.getUuid()));
                            new GUIChronomatron().open(player);
                            return;
                        }
                        if (res.complete) {
                            // Next round
                            currentInput.clear();
                            acceptingInput = false;
                            revealing = true;
                            revealIndex = 0;
                            lastHighlight = -1;
                            ProxyService svc = new ProxyService(ServiceType.EXPERIMENTATION);
                            svc.handleRequest(new ChronomatronNextRoundProtocolObject.NextRoundMessage(player.getUuid()));
                            // sequence will be re-fetched on next refresh
                            // Reset tiles for next reveal
                            for (int j = 0; j < gridSlots.length; j++) set(gridSlots[j], ItemStackCreator.getStack(" ", baseColorForIndex(j), 1));
                            // Reset timer for next round
                            acceptingInput = false;
                            timerSecondsRemaining = secondsForTier();
                            timerCycleCounter = 0;
                            set(timerSlot, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
                        }
                    });
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(" ", baseColorForIndex(index), 1);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() { return false; }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) { }

    @Override
    public void refreshItems(HypixelPlayer player) {
        ProxyService service = new ProxyService(ServiceType.EXPERIMENTATION);
        service.handleRequest(new ChronomatronGetStateProtocolObject.GetStateMessage(player.getUuid())).thenAccept(response -> {
            ChronomatronGetStateProtocolObject.GetStateResponse state = (ChronomatronGetStateProtocolObject.GetStateResponse) response;
            if (!state.success) return;
            this.sequence = state.sequence;
        });

        if (revealing) {
            tickCounter++;
            if (tickCounter >= ticksPerReveal) {
                tickCounter = 0;
                // Unhighlight previous
                if (lastHighlight != -1) {
                    int slot = gridSlots[lastHighlight];
                    set(slot, ItemStackCreator.getStack(" ", baseColorForIndex(lastHighlight), 1));
                }
                if (revealIndex < sequence.size()) {
                    int index = sequence.get(revealIndex);
                    lastHighlight = index;
                    int slot = gridSlots[index];
                    set(slot, ItemStackCreator.enchant(ItemStackCreator.getStack(" ", baseColorForIndex(index), 1)));
                    // Play a reveal pling with ascending pitch
                    float revealPitch = 0.8f + (revealIndex * 0.05f);
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, revealPitch));
                    revealIndex++;
                } else {
                    // End of reveal, clear highlight and accept input
                    if (lastHighlight != -1) {
                        int slot = gridSlots[lastHighlight];
                        set(slot, ItemStackCreator.getStack(" ", baseColorForIndex(lastHighlight), 1));
                        lastHighlight = -1;
                    }
                    revealing = false;
                    acceptingInput = true;
                    // Start round timer
                    timerSecondsRemaining = secondsForTier();
                    timerCycleCounter = 0;
                }
            }
        }

        // Timer update while accepting input
        if (acceptingInput) {
            // 20 ticks per second; refresh is every 2 ticks => 10 cycles per second
            timerCycleCounter++;
            if (timerCycleCounter >= 10) {
                timerCycleCounter = 0;
                if (timerSecondsRemaining > 0) timerSecondsRemaining--;
            }
        }

        // Always update timer display
        set(timerSlot, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
    }

    @Override
    public int refreshRate() {
        return 2; // fast updates
    }

    private Material baseColorForIndex(int index) {
        // Map 3x3 positions to columns 0,1,2
        int column = index % 3;
        return colorPalette[column];
    }

    private int secondsForTier() {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("high")) return 45;
        if (t.contains("medium") || t.contains("med")) return 35;
        return 30; // low/default
    }
}



