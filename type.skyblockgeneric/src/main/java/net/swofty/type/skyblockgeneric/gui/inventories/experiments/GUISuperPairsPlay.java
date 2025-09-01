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
import java.util.List;

public class GUISuperPairsPlay extends HypixelInventoryGUI implements RefreshingGUI {

    private final String tier;
    private final int[] borderSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
    // 4x4 grid centered in a 6-row GUI
    private final int[] gridSlots = {10, 11, 12, 13, 19, 20, 21, 22, 28, 29, 30, 31, 37, 38, 39, 40};

    private volatile List<String> boardLayout = new ArrayList<>();
    private volatile boolean revealedTiles = false;
    private volatile int firstFlipIndex = -1;
    private volatile int clicksRemaining = 0;
    private volatile int pairsFound = 0;
    private volatile boolean needsState = true;
    private volatile boolean gameComplete = false;

    // Timer display
    private final int timerSlot = 4;
    private volatile int timerSecondsRemaining = 0;
    private int timerCycleCounter = 0;

    public GUISuperPairsPlay(String tier) {
        super("SuperPairs (" + tier + ")", InventoryType.CHEST_6_ROW);
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

            // Initialize 4x4 grid with hidden tiles
            for (int i = 0; i < gridSlots.length; i++) {
                set(gridSlots[i], ItemStackCreator.getStack("§7Hidden Tile", Material.GRAY_STAINED_GLASS_PANE, 1));
            }

            // Initialize timer display
            timerSecondsRemaining = secondsForTier();
            set(timerSlot, ItemStackCreator.getStack("§eClicks Left", Material.BOOKSHELF, Math.max(1, timerSecondsRemaining)));

        } catch (Exception ex) {
            e.player().sendMessage("§cFailed to open SuperPairs: " + ex.getMessage());
        }

        set(GUIClickableItem.getGoBackItem(49, new GUISuperPairs()));

        // Grid click handlers
        for (int i = 0; i < gridSlots.length; i++) {
            final int index = i;
            set(new GUIClickableItem(gridSlots[i]) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    if (gameComplete || clicksRemaining <= 0) return;

                    ExperimentationManager.SuperPairsFlipResult result = ExperimentationManager.flipTile(player, index);
                    if (!result.success) {
                        player.sendMessage("§cError: " + result.errorMessage);
                        return;
                    }

                    // Update local state
                    clicksRemaining = result.clicksRemaining;
                    pairsFound = result.pairsFound;
                    gameComplete = result.gameComplete;

                    // Play sound based on result
                    if (result.isMatch) {
                        player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 1.2f));
                        player.sendMessage("§aMatch found! §7+" + result.itemType);
                    } else {
                        player.playSound(Sound.sound(Key.key("block.note_block.bass"), Sound.Source.PLAYER, 1.0f, 0.8f));
                    }

                    // Check if game is complete
                    if (gameComplete) {
                        player.sendMessage("§aSuperPairs Complete! §7Pairs Found: §e" + pairsFound);
                        // Finish the game
                        ExperimentationManager.SuperPairsFinishResult finishResult = ExperimentationManager.finishSuperPairs(player);
                        if (finishResult.success) {
                            player.sendMessage("§aSuperPairs Complete! §7Pairs: §e" + finishResult.pairsFound + " §7| §b+" + finishResult.xpAward + " Enchanting XP");
                            if (finishResult.bonusClicksEarned > 0) {
                                player.sendMessage("§aBonus Clicks Earned: §e+" + finishResult.bonusClicksEarned);
                            }
                        }
                        new GUISuperPairs().open(player);
                    }
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    if (boardLayout.size() <= index) {
                        return ItemStackCreator.getStack("§7Hidden Tile", Material.GRAY_STAINED_GLASS_PANE, 1);
                    }

                    String itemType = boardLayout.get(index);
                    Material material = getMaterialForItemType(itemType);
                    
                    if (revealedTiles || firstFlipIndex == index) {
                        return ItemStackCreator.getStack("§f" + itemType, material, 1);
                    } else {
                        return ItemStackCreator.getStack("§7Hidden Tile", Material.GRAY_STAINED_GLASS_PANE, 1);
                    }
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
            GameSession.SuperPairsState state = ExperimentationManager.getSuperPairsState(player);
            if (state != null) {
                this.boardLayout = state.boardLayout;
                this.revealedTiles = state.revealedTiles;
                this.firstFlipIndex = state.firstFlipIndex;
                this.clicksRemaining = state.clicksRemaining;
                this.pairsFound = state.pairsFound;
                this.needsState = false;
            }
        }

        // Update timer display
        set(timerSlot, ItemStackCreator.getStack("§eClicks Left", Material.BOOKSHELF, Math.max(1, clicksRemaining)));
    }

    @Override
    public int refreshRate() {
        return 10;
    }

    private Material getMaterialForItemType(String itemType) {
        switch (itemType) {
            case "DIAMOND": return Material.DIAMOND;
            case "EMERALD": return Material.EMERALD;
            case "GOLD_INGOT": return Material.GOLD_INGOT;
            case "IRON_INGOT": return Material.IRON_INGOT;
            case "COAL": return Material.COAL;
            case "REDSTONE": return Material.REDSTONE;
            case "LAPIS_LAZULI": return Material.LAPIS_LAZULI;
            case "QUARTZ": return Material.QUARTZ;
            default: return Material.STONE;
        }
    }

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
