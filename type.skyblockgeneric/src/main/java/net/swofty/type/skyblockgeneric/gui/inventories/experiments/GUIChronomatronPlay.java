package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.experimentation.ExperimentationManager;
import net.swofty.type.skyblockgeneric.experimentation.GameSession;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.*;

public class GUIChronomatronPlay extends HypixelInventoryGUI implements RefreshingGUI {

    private final String tier;

    private static final int TIMER_SLOT = 4;

    private final Material[] colorPalette;
    private final int colorCount;

    private final Map<Integer, Integer> slotToColorIndex = new HashMap<>();
    private final Map<Integer, List<Integer>> colorIndexToSlots = new HashMap<>();

    private volatile boolean isSequencePlaying = false;
    private volatile int currentSequenceIndex = 0;
    private volatile int sequenceDisplayDelay = 0;
    private volatile int highlightedColor = -1;
    private volatile boolean gameOver = false;

    private static final int SEQUENCE_DISPLAY_DELAY = 6;
    private static final int COLOR_HIGHLIGHT_DURATION = 3;

    // Timer state
    private volatile int timerSecondsRemaining = 0;
    private volatile long lastTimerUpdateMs = 0L;

    public GUIChronomatronPlay(String tier) {
        super("Chronomatron (" + toTitleCase(tier) + ")", InventoryType.CHEST_6_ROW);
        this.tier = tier;

        ExperimentationManager.GameTier gameTier = ExperimentationManager.TIERS.get(tier.toUpperCase());
        if (gameTier == null) gameTier = ExperimentationManager.TIERS.get("HIGH");

        this.colorCount = gameTier.colors;
        this.colorPalette = new Material[colorCount];

        Material[] allColors = {
            Material.RED_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS,
            Material.LIME_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS,
            Material.WHITE_STAINED_GLASS,
            Material.PURPLE_STAINED_GLASS,
            Material.ORANGE_STAINED_GLASS,
            Material.PINK_STAINED_GLASS,
            Material.LIGHT_BLUE_STAINED_GLASS,
            Material.CYAN_STAINED_GLASS
        };
        for (int i = 0; i < colorCount; i++) colorPalette[i] = allColors[i];

        buildTierLayoutMapping();
    }

    private void buildTierLayoutMapping() {
        String t = tier == null ? "" : tier.toLowerCase();
        java.util.function.BiConsumer<Integer, Integer> register = (slot, colorIdx) -> {
            slotToColorIndex.put(slot, colorIdx);
            colorIndexToSlots.computeIfAbsent(colorIdx, k -> new ArrayList<>()).add(slot);
        };
        if (t.contains("metaphysical")) {
            for (int col = 0; col < 5; col++) { register.accept(11 + col, col); register.accept(20 + col, col); }
            for (int col = 0; col < 5; col++) { register.accept(29 + col, 5 + col); register.accept(38 + col, 5 + col); }
        } else if (t.contains("transcendent")) {
            for (int col = 0; col < 4; col++) { register.accept(11 + col, col); register.accept(20 + col, col); }
            for (int col = 0; col < 4; col++) { register.accept(30 + col, 4 + col); register.accept(39 + col, 4 + col); }
        } else if (t.contains("supreme")) {
            for (int base : new int[]{10, 19, 28}) for (int col = 0; col < 7; col++) register.accept(base + col, col);
        } else if (t.contains("grand")) {
            for (int base : new int[]{11, 20, 29}) for (int col = 0; col < 5; col++) register.accept(base + col, col);
        } else {
            for (int base : new int[]{12, 21, 30}) for (int col = 0; col < 3; col++) register.accept(base + col, col);
        }
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.GRAY_STAINED_GLASS_PANE, " ");
        timerSecondsRemaining = secondsForTier();
        lastTimerUpdateMs = System.currentTimeMillis();
        set(TIMER_SLOT, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));

        GameSession.ChronomatronState existing = ExperimentationManager.getChronomatronState(e.player());
        if (existing == null) {
            if (!ExperimentationManager.startChronomatron(e.player(), tier)) {
                e.player().sendMessage("§cFailed to start Chronomatron session.");
                return;
            }
        }
        // Always start the first round here (manager no longer auto-appends). Ensure fresh reveal counters.
        startNextRound(e.player());

        // Register click handlers for each color slot to process inputs and advance rounds
        for (Map.Entry<Integer, Integer> entry : slotToColorIndex.entrySet()) {
            final int slot = entry.getKey();
            final int colorIdx = entry.getValue();
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent clickEvent, HypixelPlayer player) {
                    if (gameOver || isSequencePlaying) return;
                    GameSession.ChronomatronState state = ExperimentationManager.getChronomatronState(player);
                    if (state == null || state.gameState != GameSession.ChronomatronState.GameStateEnum.PLAYING) return;

                    var result = ExperimentationManager.inputChronomatron(player, colorIdx);
                    if (!result.success) {
                        player.sendMessage("§cError: " + result.errorMessage);
                        return;
                    }

                    if (result.correct) {
                        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 1.0f));
                        if (result.complete) {
                            startNextRound(player);
                        }
                    } else {
                        player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 1.0f, 0.5f));
                        gameOver = true;
                        showGameOver(player);
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    Material material = colorPalette[colorIdx % colorCount];
                    String name = "§" + getColorCode(colorIdx) + "Color " + (colorIdx + 1) + (highlightedColor == colorIdx ? " §e(Selected)" : "");
                    ItemStack.Builder base = ItemStackCreator.getStack(name, material, 1);
                    return (highlightedColor == colorIdx) ? ItemStackCreator.enchant(base) : base;
                }
            });
        }

        // Render initial items for all GUI entries
        updateItemStacks(getInventory(), getPlayer());
    }

    private void startNextRound(HypixelPlayer player) {
        GameSession.ChronomatronState state = ExperimentationManager.getChronomatronState(player);
        if (state == null) return;
        ExperimentationManager.startNewChronomatronRound(player);
        // Reset reveal counters to begin from the first element of the new sequence
        isSequencePlaying = true;
        currentSequenceIndex = 0;
        sequenceDisplayDelay = 0;
        highlightedColor = -1;
        // Reset round timer
        timerSecondsRemaining = secondsForTier();
        lastTimerUpdateMs = System.currentTimeMillis();
        player.sendMessage("§7Revealing sequence (len=" + state.correctSequence.size() + ")...");
    }

    private void showGameOver(HypixelPlayer player) {
        var result = ExperimentationManager.finishChronomatron(player);
        if (result.success) new GUIExperimentOver("Chronomatron", tier, false, "Game Over", result.bestChain, result.xpAward, result.bonusClicksEarned).open(player);
    }

    private String getColorCode(int colorIndex) {
        String[] codes = {"c", "9", "a", "e", "f", "5", "6", "d", "b", "3"};
        return codes[colorIndex % codes.length];
    }

    private static String toTitleCase(String str) { if (str == null || str.isEmpty()) return str; return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase(); }

    @Override public int refreshRate() { return 2; }

    @Override
    public void refreshItems(HypixelPlayer player) {
        if (isSequencePlaying) {
            var state = ExperimentationManager.getChronomatronState(player); if (state == null) return;
            sequenceDisplayDelay++;
            if (sequenceDisplayDelay >= SEQUENCE_DISPLAY_DELAY) {
                if (currentSequenceIndex < state.correctSequence.size()) {
                    int colorIndex = state.correctSequence.get(currentSequenceIndex);
                    highlightedColor = colorIndex;
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.MASTER, 1.0f, 0.8f + (currentSequenceIndex * 0.05f)));
                    currentSequenceIndex++;
                    sequenceDisplayDelay = 0;
                } else {
                    isSequencePlaying = false;
                    highlightedColor = -1;
                    ExperimentationManager.onChronomatronSequenceComplete(player);
                    player.sendMessage("§aNow repeat the sequence!");
                }
            } else if (sequenceDisplayDelay >= COLOR_HIGHLIGHT_DURATION) {
                highlightedColor = -1;
            }
        }

        // Decrement timer only while awaiting player input
        GameSession.ChronomatronState stateNow = ExperimentationManager.getChronomatronState(player);
        if (stateNow != null && stateNow.gameState == GameSession.ChronomatronState.GameStateEnum.PLAYING) {
            long now = System.currentTimeMillis();
            if (now - lastTimerUpdateMs >= 1000) {
                lastTimerUpdateMs = now;
                if (timerSecondsRemaining > 0) timerSecondsRemaining--;
                if (timerSecondsRemaining <= 0 && !gameOver) {
                    // Time ran out -> end game
                    gameOver = true;
                    showGameOver(player);
                }
            }
        }

        // Update dynamic items (clickables render based on highlightedColor)
        updateItemStacks(getInventory(), player);
        set(TIMER_SLOT, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
    }

    @Override public boolean allowHotkeying() { return false; }
    @Override public void onBottomClick(InventoryPreClickEvent e) { e.setCancelled(true); }

    private int secondsForTier() {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("high")) return 20;
        if (t.contains("grand")) return 18;
        if (t.contains("supreme")) return 16;
        if (t.contains("transcendent")) return 14;
        if (t.contains("metaphysical")) return 12;
        return 20; // default
    }
}



