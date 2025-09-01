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

import java.util.HashMap;
import java.util.Map;

public class GUIUltrasequencerPlay extends HypixelInventoryGUI implements RefreshingGUI {

    private final String tier;
    // 3x3 grid centered in a 6-row GUI
    private final int[] gridSlots = {12, 13, 14, 21, 22, 23, 30, 31, 32};

    private volatile Map<Integer, Integer> pattern = new HashMap<>();
    private volatile int playerInputPosition = 1;
    private volatile int currentSeriesLength = 0;
    private volatile boolean needsState = true;
    private volatile boolean revealing = true;
    private volatile int revealIndex = 0;
    private volatile int tickCounter = 0;
    private final int ticksPerReveal = 20; // 1 second
    private volatile boolean acceptingInput = false;
    private volatile boolean gameComplete = false;

    public GUIUltrasequencerPlay(String tier) {
        super("Ultrasequencer (" + tier + ")", InventoryType.CHEST_6_ROW);
        this.tier = tier;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        try {
            fill(Material.BLACK_STAINED_GLASS_PANE, " ");

            // Initialize 3x3 grid with numbers 1-9
            for (int i = 0; i < gridSlots.length; i++) {
                set(gridSlots[i], ItemStackCreator.getStack("§f" + (i + 1), Material.WHITE_STAINED_GLASS_PANE, i + 1));
            }

        } catch (Exception ex) {
            e.player().sendMessage("§cFailed to open Ultrasequencer: " + ex.getMessage());
        }

        set(GUIClickableItem.getGoBackItem(49, new GUIUltrasequencer()));

        // Grid click handlers
        for (int i = 0; i < gridSlots.length; i++) {
            final int slotIndex = i + 1; // 1-9
            set(new GUIClickableItem(gridSlots[i]) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    if (!acceptingInput || gameComplete) return;

                    ExperimentationManager.UltrasequencerInputResult result = ExperimentationManager.inputUltrasequencer(player, slotIndex);
                    if (!result.success) {
                        player.sendMessage("§cError: " + result.errorMessage);
                        return;
                    }

                    // Play sound based on result
                    if (result.correct) {
                        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 1.0f));
                    } else {
                        player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 1.0f, 0.5f));
                        // Finish and show results
                        var finishResult = ExperimentationManager.finishUltrasequencer(player);
                        if (finishResult.success) new GUIExperimentOver("Ultrasequencer", tier, false, "Game Over", finishResult.bestSeriesLength, finishResult.xpAward, finishResult.bonusClicksEarned).open(player);
                        return;
                    }

                    if (result.complete) {
                        // Next round
                        acceptingInput = false;
                        revealing = true;
                        revealIndex = 0;
                        tickCounter = 0;
                        
                        // Flag to pull new state and start next reveal on refresh
                        needsState = true;
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    if (revealing && revealIndex < currentSeriesLength) {
                        Integer expectedNumber = pattern.get(revealIndex);
                        if (expectedNumber != null && expectedNumber == slotIndex) {
                            return ItemStackCreator.enchant(ItemStackCreator.getStack("§f" + slotIndex, Material.YELLOW_STAINED_GLASS_PANE, slotIndex));
                        }
                    }
                    return ItemStackCreator.getStack("§f" + slotIndex, Material.WHITE_STAINED_GLASS_PANE, slotIndex);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        if (needsState) {
            GameSession.UltraSequencerState state = ExperimentationManager.getUltrasequencerState(player);
            if (state != null) {
                this.pattern = state.pattern;
                this.playerInputPosition = state.playerInputPosition;
                this.currentSeriesLength = state.currentSeriesLength;
                
                // Prepare for reveal once we have data
                revealIndex = 0;
                tickCounter = 0;
                acceptingInput = false;
                revealing = true;
                needsState = false;
            }
        }

        if (revealing) {
            tickCounter++;
            if (tickCounter >= ticksPerReveal) {
                tickCounter = 0;
                
                if (pattern == null || pattern.isEmpty()) {
                    return;
                }
                
                if (revealIndex < currentSeriesLength) {
                    // Play a reveal sound
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 0.8f + (revealIndex * 0.1f)));
                    revealIndex++;
                } else {
                    // End of reveal, accept input
                    revealing = false;
                    acceptingInput = true;
                }
            }
        }
    }

    @Override
    public int refreshRate() {
        return 2; // fast updates
    }

}

