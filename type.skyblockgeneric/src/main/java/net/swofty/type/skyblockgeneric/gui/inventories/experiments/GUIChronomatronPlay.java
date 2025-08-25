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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GUIChronomatronPlay extends HypixelInventoryGUI implements RefreshingGUI {

    private final String tier;
    private final int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
    // Dynamic grid slots based on tier
    private final int[] gridSlots;
    private final int gridColumns;
    private final int gridRows;

    // Full color palette supporting up to 20 colors for Metaphysical tier
    private final Material[] fullColorPalette = new Material[] {
            Material.RED_STAINED_GLASS,      // 0 (col 0 - red)
            Material.BLUE_STAINED_GLASS,     // 1 (col 1 - blue) 
            Material.LIME_STAINED_GLASS,     // 2 (col 2 - green)
            Material.YELLOW_STAINED_GLASS,   // 3 (col 3 - yellow)
            Material.WHITE_STAINED_GLASS,    // 4 (col 4 - white)
            Material.PURPLE_STAINED_GLASS,   // 5 (col 5 - purple)
            Material.ORANGE_STAINED_GLASS,   // 6 (col 6 - orange)
            Material.PINK_STAINED_GLASS,     // 7 (col 7 - pink)
            Material.LIGHT_BLUE_STAINED_GLASS, // 8 (col 8 - light blue)
            Material.CYAN_STAINED_GLASS,     // 9 (col 9 - cyan)
            Material.LIGHT_GRAY_STAINED_GLASS, // 10 (col 10 - light gray)
            Material.GRAY_STAINED_GLASS,     // 11 (col 11 - gray)
            Material.BROWN_STAINED_GLASS,    // 12 (col 12 - brown)
            Material.MAGENTA_STAINED_GLASS,  // 13 (col 13 - magenta)
            Material.LIME_STAINED_GLASS,     // 14 (col 14 - lime)
            Material.BLUE_STAINED_GLASS,     // 15 (col 15 - blue)
            Material.YELLOW_STAINED_GLASS,   // 16 (col 16 - yellow)
            Material.WHITE_STAINED_GLASS,    // 17 (col 17 - white)
            Material.PURPLE_STAINED_GLASS,   // 18 (col 18 - purple)
            Material.ORANGE_STAINED_GLASS,   // 19 (col 19 - orange)
            Material.PINK_STAINED_GLASS      // 20 (col 20 - pink)
    };
    
    // Tier-specific color palette
    private final Material[] colorPalette;

    private volatile List<Integer> sequence = new ArrayList<>();
    private volatile boolean revealing = true;
    private volatile int revealIndex = 0;
    private volatile int tickCounter = 0;
    private final int ticksPerReveal = 3; // much faster reveals
    private volatile int lastHighlight = -1;
    private volatile int uiHighlightIndex = -1; // drives rendering in getItem
    private volatile boolean acceptingInput = false;
    private volatile int highlightClearDelay = 0; // delay counter for clearing highlights
    private final List<Integer> currentInput = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean needsState = true; // gate network fetch and reveal start

    // Timer (bookshelf) – counts down during input
    private final int timerSlot = 4;
    private volatile int timerSecondsRemaining = 0;
    private int timerCycleCounter = 0; // counts refresh cycles; 10 cycles ~= 1 second at refreshRate()=2

    public GUIChronomatronPlay(String tier) {
        super("Chronomatron (" + toTitleCase(tier) + ")", InventoryType.CHEST_6_ROW);
        this.tier = tier;
        
        // Initialize grid based on tier
        this.gridColumns = getColumnsForTier(tier);
        this.gridRows = getRowsForTier(tier);
        this.gridSlots = generateGridSlots(gridColumns).stream().mapToInt(i -> i).toArray();
        this.colorPalette = new Material[gridColumns];
        System.arraycopy(fullColorPalette, 0, colorPalette, 0, gridColumns);
    }
    
    private int getColumnsForTier(String tier) {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("metaphysical")) return 20;  // 20 total slots for new layout
        if (t.contains("transcendent")) return 16;  // 16 total slots for new layout
        if (t.contains("supreme")) return 7;
        if (t.contains("grand")) return 5;
        if (t.contains("high")) return 3;
        return 3; // default
    }
    
    private List<Integer> generateGridSlots(int columns) {
        String t = tier == null ? "" : tier.toLowerCase();
        
        if (t.contains("transcendent")) {
            // New layout: 16 slots with 8 distinct colors (each color appears twice)
            // Top row: slots 11, 12, 13, 14
            // Bottom row: slots 20, 21, 22, 23
            // Additional rows: slots 30, 31, 32, 33 and 39, 40, 41, 42
            return Arrays.asList(11, 12, 13, 14, 20, 21, 22, 23, 30, 31, 32, 33, 39, 40, 41, 42);
        } else if (t.contains("metaphysical")) {
            // Metaphysical: 10 color pairs (20 total slots)
            // Pairs: 11/20, 12/21, 13/22, 14/23, 15/24, 30/39, 31/40, 32/41, 33/42, 34/43
            return Arrays.asList(11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42);
        } else {
            // Standard tiers: dynamic grid generation with tier-specific offsets
            List<Integer> slots = new ArrayList<>();
            int startSlot;
            
            // Apply tier-specific horizontal offsets
            if (t.contains("high")) {
                startSlot = 12; // One slot to the left
            } else if (t.contains("supreme")) {
                startSlot = 10; // One slot to the right
            } else {
                startSlot = 11; // Default (Grand and others)
            }
            
            int currentSlot = startSlot;
            
            for (int row = 0; row < gridRows; row++) {
                for (int col = 0; col < columns; col++) {
                    slots.add(currentSlot);
                    currentSlot++;
                }
                // Move to next row (skip to next row's starting position)
                currentSlot = startSlot + (row + 1) * 9;
            }
            return slots;
        }
    }
    
    private int getRowsForTier(String tier) {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("metaphysical")) return 5;
        if (t.contains("transcendent")) return 5;
        return 3; // default for other tiers
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
            // Initialize dynamic grid with base colors
            for (int i = 0; i < gridSlots.length; i++) {
                set(gridSlots[i], ItemStackCreator.getStack(" ", getDualColorItem(i), 1));
            }
            // Initialize timer display (bookshelf) – idle until reveal completes
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

                    // No highlight feedback on click - remove highlighting
                    uiHighlightIndex = -1;

                    // Check if we have collected the complete sequence
                    if (currentInput.size() >= sequence.size()) {
                        // Validate the complete sequence
                        ExperimentationManager.ChronomatronInputResult result = ExperimentationManager.inputChronomatron(player, new ArrayList<>(currentInput));
                        if (!result.success) {
                            player.sendMessage("§cError: " + result.errorMessage);
                            return;
                        }
                        
                        if (!result.correct) {
                            player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 1.0f, 0.5f));
                            // On failure, finish session and award, then show experiment over screen
                            ExperimentationManager.ChronomatronFinishResult finishResult = ExperimentationManager.finishChronomatron(player);
                            if (finishResult.success) {
                                new GUIExperimentOver("Chronomatron", tier, false, "You picked the wrong color", 
                                    finishResult.bestChain, finishResult.xpAward, finishResult.bonusClicksEarned).open(player);
                            } else {
                                new GUIChronomatron().open(player);
                            }
                            return;
                        }
                        
                        if (result.correct) {
                            // Next round - update sequence with the new length from result
                            // Get the updated sequence from the backend
                            GameSession.ChronomatronState state = ExperimentationManager.getChronomatronState(player);
                            if (state != null && state.correctSequence != null) {
                                // Update the sequence field through a synchronized method
                                updateSequence(state.correctSequence);
                                player.sendMessage("§7Sequence updated: §e" + state.correctSequence.size() + " §7colors");
                                
                                // Reset for next reveal
                                currentInput.clear();
                                acceptingInput = false;
                                revealing = true;
                                revealIndex = 0;
                                
                                // Ensure all highlighting is completely cleared for next reveal
                                lastHighlight = -1;
                                uiHighlightIndex = -1;
                                highlightClearDelay = 0;
                                
                                needsState = false; // Don't need to fetch state again
                                
                                // Reset tiles for next reveal
                                for (int j = 0; j < gridSlots.length; j++) {
                                    set(gridSlots[j], ItemStackCreator.getStack(" ", baseColorForIndex(j), 1));
                                }
                                
                                // Reset timer for next round
                                timerSecondsRemaining = secondsForTier();
                                timerCycleCounter = 0;
                                set(timerSlot, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
                                
                                // Play success sound
                                player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 1.5f));
                                player.sendMessage("§aCorrect! §7Next round starting...");
                            } else {
                                // Fallback if state is null - try to continue with current sequence
                                player.sendMessage("§cWarning: Could not update sequence, continuing with current...");
                                currentInput.clear();
                                acceptingInput = true;
                            }
                        }
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    // Only show highlighting during reveal phase, never during input phase
                    if (revealing && uiHighlightIndex != -1 && shouldHighlightSlot(index, uiHighlightIndex)) {
                        return ItemStackCreator.enchant(ItemStackCreator.getStack(" ", getDualColorItem(index), 1));
                    }
                    return ItemStackCreator.getStack(" ", getDualColorItem(index), 1);
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
        if (needsState) {
            GameSession.ChronomatronState state = ExperimentationManager.getChronomatronState(player);
            if (state != null && state.correctSequence != null && !state.correctSequence.isEmpty()) {
                updateSequence(state.correctSequence);
                // Prepare for reveal once we have data
                revealIndex = 0;
                tickCounter = 0;
                lastHighlight = -1;
                acceptingInput = false;
                revealing = true;
                needsState = false;
                player.sendMessage("§7Game started with §e" + state.correctSequence.size() + " §7colors");
            }
        }

        if (revealing) {
            tickCounter++;
            if (tickCounter >= ticksPerReveal) {
                tickCounter = 0;
                
                // Handle highlight clearing with delay
                if (highlightClearDelay > 0) {
                    highlightClearDelay--;
                    if (highlightClearDelay == 0) {
                        uiHighlightIndex = -1;
                    }
                    return; // Don't proceed to next reveal while clearing
                }
                
                // Wait until we actually have a sequence before attempting to reveal
                if (sequence == null || sequence.isEmpty()) {
                    return;
                }
                
                // Safety check: ensure revealIndex is within bounds
                if (revealIndex >= sequence.size()) {
                    // This shouldn't happen, but if it does, reset to input mode
                    revealing = false;
                    acceptingInput = true;
                    timerSecondsRemaining = secondsForTier();
                    timerCycleCounter = 0;
                    return;
                }
                
                // Debug: log sequence info
                if (revealIndex == 0) {
                    player.sendMessage("§7Starting reveal of §e" + sequence.size() + " §7colors");
                }
                
                if (revealIndex < sequence.size()) {
                    int index = sequence.get(revealIndex);
                    
                    // If same color as previous, add clear delay
                    if (lastHighlight != -1) {
                        boolean sameColor = false;
                        String t = tier == null ? "" : tier.toLowerCase();
                        
                        if (t.contains("transcendent") || t.contains("metaphysical")) {
                            // For dual-color tiers, check if both indices belong to the same color group
                            int currentColorIndex = index / 2;
                            int lastColorIndex = lastHighlight / 2;
                            sameColor = (currentColorIndex == lastColorIndex);
                        } else {
                            // Standard tiers use the old logic
                            sameColor = (baseColorForIndex(index) == baseColorForIndex(lastHighlight));
                        }
                        
                        if (sameColor && uiHighlightIndex != -1) {
                            // Start clear delay
                            highlightClearDelay = 1; // 1 tick delay for faster transitions
                            return;
                        }
                    }
                    
                    lastHighlight = index;
                    uiHighlightIndex = index;
                    // Play a reveal pling with ascending pitch
                    float revealPitch = 0.8f + (revealIndex * 0.05f);
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, revealPitch));
                    revealIndex++;
                } else {
                    // End of reveal, clear highlight and accept input
                    // Ensure all highlighting is completely cleared
                    lastHighlight = -1;
                    uiHighlightIndex = -1;
                    highlightClearDelay = 0;
                    
                    revealing = false;
                    acceptingInput = true;
                    
                    // Force a visual refresh to clear any remaining highlights
                    player.sendMessage("§7Your turn! §eRepeat the sequence!");
                    
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
                if (timerSecondsRemaining > 0) {
                    timerSecondsRemaining--;
                } else {
                    // Time's up! End the game
                    acceptingInput = false;
                    player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 1.0f, 0.5f));
                    ExperimentationManager.ChronomatronFinishResult finishResult = ExperimentationManager.finishChronomatron(player);
                    if (finishResult.success) {
                        new GUIExperimentOver("Chronomatron", tier, false, "Time's up!", 
                            finishResult.bestChain, finishResult.xpAward, finishResult.bonusClicksEarned).open(player);
                    } else {
                        new GUIChronomatron().open(player);
                    }
                    return;
                }
            }
        }

        // Always update timer display
        String timerText;
        if (acceptingInput) {
            timerText = "§eTime Left: §f" + timerSecondsRemaining + "s";
        } else if (revealing) {
            timerText = "§7Revealing...";
        } else {
            timerText = "§7Waiting...";
        }
        set(timerSlot, ItemStackCreator.getStack(timerText, Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
    }

    @Override
    public int refreshRate() {
        return 2; // fast updates
    }
    
    // Helper method to update sequence safely
    private synchronized void updateSequence(List<Integer> newSequence) {
        this.sequence.clear();
        this.sequence.addAll(newSequence);
    }

    private Material baseColorForIndex(int index) {
        // Map grid positions to columns based on grid size
        int column = index % gridColumns;
        return colorPalette[column];
    }
    
    private Material getDualColorItem(int index) {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("transcendent")) {
            // New layout: 8 distinct colors
            // Red: slots 11, 20
            // Blue: slots 12, 21
            // Green: slots 13, 22
            // Yellow: slots 14, 23
            // Cyan: slots 30, 39
            // Purple: slots 31, 40
            // Orange: slots 32, 41
            // Pink: slots 33, 42
            
            // Map slot indices to colors (8 unique colors, each appearing in 2 slots)
            if (index == 0 || index == 4) return Material.RED_STAINED_GLASS;      // Slots 11, 20: Red
            if (index == 1 || index == 5) return Material.BLUE_STAINED_GLASS;     // Slots 12, 21: Blue
            if (index == 2 || index == 6) return Material.LIME_STAINED_GLASS;     // Slots 13, 22: Green
            if (index == 3 || index == 7) return Material.YELLOW_STAINED_GLASS;   // Slots 14, 23: Yellow
            if (index == 8 || index == 12) return Material.CYAN_STAINED_GLASS;    // Slots 30, 39: Cyan
            if (index == 9 || index == 13) return Material.PURPLE_STAINED_GLASS;  // Slots 31, 40: Purple
            if (index == 10 || index == 14) return Material.ORANGE_STAINED_GLASS; // Slots 32, 41: Orange
            if (index == 11 || index == 15) return Material.PINK_STAINED_GLASS;   // Slots 33, 42: Pink
            
            return Material.GRAY_STAINED_GLASS;
        } else if (t.contains("metaphysical")) {
            // Metaphysical: 10 color pairs (20 total slots)
            // Pairs: 11/20, 12/21, 13/22, 14/23, 15/24, 30/39, 31/40, 32/41, 33/42, 34/43
            // Map slot indices to colors (each color appears in 2 slots)
            if (index == 0 || index == 5) return Material.RED_STAINED_GLASS;       // Slots 11, 20: Red
            if (index == 1 || index == 6) return Material.LIME_STAINED_GLASS;      // Slots 12, 21: Green
            if (index == 2 || index == 7) return Material.BLUE_STAINED_GLASS;      // Slots 13, 22: Blue
            if (index == 3 || index == 8) return Material.YELLOW_STAINED_GLASS;    // Slots 14, 23: Yellow
            if (index == 4 || index == 9) return Material.WHITE_STAINED_GLASS;     // Slots 15, 24: White
            if (index == 10 || index == 15) return Material.PURPLE_STAINED_GLASS;  // Slots 30, 39: Purple
            if (index == 11 || index == 16) return Material.ORANGE_STAINED_GLASS;  // Slots 31, 40: Orange
            if (index == 12 || index == 17) return Material.PINK_STAINED_GLASS;    // Slots 32, 41: Pink
            if (index == 13 || index == 18) return Material.LIGHT_BLUE_STAINED_GLASS; // Slots 33, 42: Light Blue
            if (index == 14 || index == 19) return Material.LIGHT_GRAY_STAINED_GLASS; // Slots 34, 43: Light Gray
            
            return Material.GRAY_STAINED_GLASS;
        } else {
            return baseColorForIndex(index);
        }
    }

    private boolean shouldHighlightSlot(int slotIndex, int highlightedIndex) {
        String t = tier == null ? "" : tier.toLowerCase();
        
        if (t.contains("transcendent")) {
            // New layout: check if both slots belong to the same color group
            // Red: indices 0, 4 (slots 11, 20)
            // Blue: indices 1, 5 (slots 12, 21)
            // Green: indices 2, 6 (slots 13, 22)
            // Yellow: indices 3, 7 (slots 14, 23)
            // Cyan: indices 8, 12 (slots 30, 39)
            // Purple: indices 9, 13 (slots 31, 40)
            // Orange: indices 10, 14 (slots 32, 41)
            // Pink: indices 11, 15 (slots 33, 42)
            
            // Check if both slots belong to the same color group
            if ((slotIndex == 0 || slotIndex == 4) && (highlightedIndex == 0 || highlightedIndex == 4)) return true;      // Red
            if ((slotIndex == 1 || slotIndex == 5) && (highlightedIndex == 1 || highlightedIndex == 5)) return true;      // Blue
            if ((slotIndex == 2 || slotIndex == 6) && (highlightedIndex == 2 || highlightedIndex == 6)) return true;      // Green
            if ((slotIndex == 3 || slotIndex == 7) && (highlightedIndex == 3 || highlightedIndex == 7)) return true;      // Yellow
            if ((slotIndex == 8 || slotIndex == 12) && (highlightedIndex == 8 || highlightedIndex == 12)) return true;    // Cyan
            if ((slotIndex == 9 || slotIndex == 13) && (highlightedIndex == 9 || highlightedIndex == 13)) return true;    // Purple
            if ((slotIndex == 10 || slotIndex == 14) && (highlightedIndex == 10 || highlightedIndex == 14)) return true;  // Orange
            if ((slotIndex == 11 || slotIndex == 15) && (highlightedIndex == 11 || highlightedIndex == 15)) return true;  // Pink
            
            return false;
        } else if (t.contains("metaphysical")) {
            // Metaphysical: check if both slots belong to the same color group
            // Pairs: 11/20, 12/21, 13/22, 14/23, 15/24, 30/39, 31/40, 32/41, 33/42, 34/43
            if ((slotIndex == 0 || slotIndex == 5) && (highlightedIndex == 0 || highlightedIndex == 5)) return true;      // Red
            if ((slotIndex == 1 || slotIndex == 6) && (highlightedIndex == 1 || highlightedIndex == 6)) return true;      // Green
            if ((slotIndex == 2 || slotIndex == 7) && (highlightedIndex == 2 || highlightedIndex == 7)) return true;      // Blue
            if ((slotIndex == 3 || slotIndex == 8) && (highlightedIndex == 3 || highlightedIndex == 8)) return true;      // Yellow
            if ((slotIndex == 4 || slotIndex == 9) && (highlightedIndex == 4 || highlightedIndex == 9)) return true;      // White
            if ((slotIndex == 10 || slotIndex == 15) && (highlightedIndex == 10 || highlightedIndex == 15)) return true;  // Purple
            if ((slotIndex == 11 || slotIndex == 16) && (highlightedIndex == 11 || highlightedIndex == 16)) return true;  // Orange
            if ((slotIndex == 12 || slotIndex == 17) && (highlightedIndex == 12 || highlightedIndex == 17)) return true;  // Pink
            if ((slotIndex == 13 || slotIndex == 18) && (highlightedIndex == 13 || highlightedIndex == 18)) return true;  // Light Blue
            if ((slotIndex == 14 || slotIndex == 19) && (highlightedIndex == 14 || highlightedIndex == 19)) return true;  // Light Gray
            
            return false;
        } else {
            // Standard tiers use the old logic
            return baseColorForIndex(slotIndex) == baseColorForIndex(highlightedIndex);
        }
    }

    private int secondsForTier() {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("high")) return 45;
        if (t.contains("medium") || t.contains("med")) return 35;
        return 30; // low/default
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder titleCaseBuilder = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                titleCaseBuilder.append(c);
                capitalizeNext = true;
            } else if (capitalizeNext) {
                titleCaseBuilder.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                titleCaseBuilder.append(Character.toLowerCase(c));
            }
        }
        return titleCaseBuilder.toString();
    }
}



