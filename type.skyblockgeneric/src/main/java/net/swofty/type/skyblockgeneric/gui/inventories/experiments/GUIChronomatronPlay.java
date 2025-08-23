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
import java.util.Collections;
import java.util.List;

public class GUIChronomatronPlay extends HypixelInventoryGUI implements RefreshingGUI {

    private final String tier;
    private final int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
    // Dynamic grid slots based on tier
    private final int[] gridSlots;
    private final int gridColumns;
    private final int gridRows;

    // Full color palette supporting up to 9 colors for Transcendent tier
    private final Material[] fullColorPalette = new Material[] {
            Material.RED_STAINED_GLASS,      // 0 (col 0 - red)
            Material.BLUE_STAINED_GLASS,     // 1 (col 1 - blue) 
            Material.LIME_STAINED_GLASS,     // 2 (col 2 - green)
            Material.YELLOW_STAINED_GLASS,   // 3 (col 3 - yellow)
            Material.WHITE_STAINED_GLASS,    // 4 (col 4 - white)
            Material.PURPLE_STAINED_GLASS,   // 5 (col 5 - purple)
            Material.ORANGE_STAINED_GLASS,   // 6 (col 6 - orange)
            Material.PINK_STAINED_GLASS,     // 7 (col 7 - pink)
            Material.LIGHT_BLUE_STAINED_GLASS // 8 (col 8 - light blue)
    };
    
    // Tier-specific color palette
    private final Material[] colorPalette;

    private volatile List<Integer> sequence = new ArrayList<>();
    private volatile boolean revealing = true;
    private volatile int revealIndex = 0;
    private volatile int tickCounter = 0;
    private final int ticksPerReveal = 10; // half second-ish
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
        super("Chronomatron (" + tier + ")", InventoryType.CHEST_6_ROW);
        this.tier = tier;
        
        // Initialize grid based on tier
        this.gridColumns = getColumnsForTier(tier);
        this.gridRows = getRowsForTier(tier);
        this.gridSlots = generateGridSlots(gridColumns);
        this.colorPalette = new Material[gridColumns];
        System.arraycopy(fullColorPalette, 0, colorPalette, 0, gridColumns);
    }
    
    private int getColumnsForTier(String tier) {
        String t = tier == null ? "" : tier.toLowerCase();
        if (t.contains("metaphysical")) return 10;  // 10 distinct colors
        if (t.contains("transcendent")) return 8;   // 8 distinct colors
        if (t.contains("supreme")) return 7;
        if (t.contains("grand")) return 5;
        if (t.contains("high")) return 3;
        return 3; // default
    }
    
    private int[] generateGridSlots(int columns) {
        String t = tier == null ? "" : tier.toLowerCase();
        
        if (t.contains("transcendent")) {
            // Transcendent: 2 rows of 4 slots (11-14, 20-23) - vertical pairs
            return new int[]{11, 12, 13, 14, 20, 21, 22, 23};
        } else if (t.contains("metaphysical")) {
            // Metaphysical: 2 rows of 5 slots (11-15, 20-24) - vertical pairs
            return new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24};
        } else {
            // Standard tiers: 3 rows with specified columns
            int rows = getRowsForTier(tier);
            int[] slots = new int[rows * columns];
            int startCol = (9 - columns) / 2; // Center the grid horizontally
            
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    slots[row * columns + col] = (row + 1) * 9 + startCol + col + 9; // +9 to shift down one row
                }
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

                    // Brief highlight feedback on click
                    uiHighlightIndex = index;

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
                            // Next round
                            currentInput.clear();
                            acceptingInput = false;
                            revealing = true;
                            revealIndex = 0;
                            lastHighlight = -1;
                            uiHighlightIndex = -1;
                            highlightClearDelay = 0;
                            // Flag to pull new state and start next reveal on refresh
                            needsState = true;
                            // Reset tiles for next reveal
                            for (int j = 0; j < gridSlots.length; j++) set(gridSlots[j], ItemStackCreator.getStack(" ", baseColorForIndex(j), 1));
                            // Reset timer for next round
                            acceptingInput = false;
                            timerSecondsRemaining = secondsForTier();
                            timerCycleCounter = 0;
                            set(timerSlot, ItemStackCreator.getStack("§eTime Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
                        }
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    // Check if this tile should be highlighted (same color as the currently revealed color)
                    if (uiHighlightIndex != -1 && shouldHighlightSlot(index, uiHighlightIndex)) {
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
            if (state != null) {
                this.sequence = state.correctSequence;
                // Prepare for reveal once we have data
                revealIndex = 0;
                tickCounter = 0;
                lastHighlight = -1;
                acceptingInput = false;
                revealing = true;
                needsState = false;
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
                
                if (revealIndex < sequence.size()) {
                    int index = sequence.get(revealIndex);
                    
                    // If same color as previous, add clear delay
                    if (lastHighlight != -1 && baseColorForIndex(index) == baseColorForIndex(lastHighlight)) {
                        if (uiHighlightIndex != -1) {
                            // Start clear delay
                            highlightClearDelay = 3; // 3 ticks delay
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
                    if (lastHighlight != -1) {
                        lastHighlight = -1;
                        uiHighlightIndex = -1;
                        highlightClearDelay = 0;
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
        String timerText = acceptingInput ? "§eTime Left: §f" + timerSecondsRemaining + "s" : "§7Waiting...";
        set(timerSlot, ItemStackCreator.getStack(timerText, Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));
    }

    @Override
    public int refreshRate() {
        return 2; // fast updates
    }

    private Material baseColorForIndex(int index) {
        // Map grid positions to columns based on grid size
        int column = index % gridColumns;
        return colorPalette[column];
    }
    
    private Material getDualColorItem(int index) {
        // For Transcendent and Metaphysical, each slot has 2 colors
        // We'll use the primary color for the item display
        String t = tier == null ? "" : tier.toLowerCase();
        
        if (t.contains("transcendent")) {
            // Transcendent: 8 distinct colors, 2 rows of 4 slots each
            switch (index) {
                case 0: return Material.RED_STAINED_GLASS;      // Slot 11: Red
                case 1: return Material.LIME_STAINED_GLASS;     // Slot 12: Green
                case 2: return Material.BLUE_STAINED_GLASS;     // Slot 13: Blue
                case 3: return Material.YELLOW_STAINED_GLASS;   // Slot 14: Yellow
                case 4: return Material.WHITE_STAINED_GLASS;    // Slot 20: White
                case 5: return Material.PURPLE_STAINED_GLASS;   // Slot 21: Purple
                case 6: return Material.ORANGE_STAINED_GLASS;   // Slot 22: Orange
                case 7: return Material.PINK_STAINED_GLASS;     // Slot 23: Pink
                default: return Material.GRAY_STAINED_GLASS;
            }
        } else if (t.contains("metaphysical")) {
            // Metaphysical: 10 distinct colors, 2 rows of 5 slots each
            switch (index) {
                case 0: return Material.RED_STAINED_GLASS;      // Slot 11: Red
                case 1: return Material.LIME_STAINED_GLASS;     // Slot 12: Green
                case 2: return Material.BLUE_STAINED_GLASS;     // Slot 13: Blue
                case 3: return Material.YELLOW_STAINED_GLASS;   // Slot 14: Yellow
                case 4: return Material.WHITE_STAINED_GLASS;    // Slot 15: White
                case 5: return Material.PURPLE_STAINED_GLASS;   // Slot 20: Purple
                case 6: return Material.ORANGE_STAINED_GLASS;   // Slot 21: Orange
                case 7: return Material.PINK_STAINED_GLASS;     // Slot 22: Pink
                case 8: return Material.LIGHT_BLUE_STAINED_GLASS; // Slot 23: Light Blue
                case 9: return Material.GRAY_STAINED_GLASS;     // Slot 24: Gray
                default: return Material.GRAY_STAINED_GLASS;
            }
        } else {
            // Standard tiers use single colors
            return baseColorForIndex(index);
        }
    }
    
    private boolean shouldHighlightSlot(int slotIndex, int highlightedIndex) {
        String t = tier == null ? "" : tier.toLowerCase();
        
        if (t.contains("transcendent") || t.contains("metaphysical")) {
            // For dual-color tiers, check if both slots contain the same primary color
            Material slotColor = getDualColorItem(slotIndex);
            Material highlightedColor = getDualColorItem(highlightedIndex);
            return slotColor == highlightedColor;
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
}



