package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.achievement.AchievementCategory;
import net.swofty.type.generic.achievement.AchievementRegistry;
import net.swofty.type.generic.achievement.AchievementType;
import net.swofty.type.generic.achievement.PlayerAchievementHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIGameAchievements extends HypixelInventoryGUI {
    private final AchievementCategory category;

    public GUIGameAchievements(AchievementCategory category) {
        super(category.getDisplayName() + " Achievements", InventoryType.CHEST_4_ROW);
        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        PlayerAchievementHandler handler = player.getAchievementHandler();

        int totalUnlocked = handler.getUnlockedCount(category);
        int totalCount = AchievementRegistry.getByCategory(category).size();
        int totalPoints = handler.getTotalPoints(category);
        int maxPoints = AchievementRegistry.getTotalPoints(category);
        double unlockedPercent = totalCount > 0 ? (totalUnlocked * 100.0 / totalCount) : 0;
        double pointsPercent = maxPoints > 0 ? (totalPoints * 100.0 / maxPoints) : 0;

        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int unlocked = handler.getUnlockedCount(category, AchievementType.CHALLENGE);
                int total = AchievementRegistry.getCount(category, AchievementType.CHALLENGE);
                int points = handler.getPoints(category, AchievementType.CHALLENGE);
                int maxPts = AchievementRegistry.getTotalPoints(category, AchievementType.CHALLENGE);
                double uPercent = total > 0 ? (unlocked * 100.0 / total) : 0;
                double pPercent = maxPts > 0 ? (points * 100.0 / maxPts) : 0;

                return ItemStackCreator.getStack(
                        "§aChallenge Achievements",
                        Material.DIAMOND,
                        1,
                        "§8" + category.getDisplayName(),
                        "§7Unlocked: §b" + unlocked + "§7/§b" + total + " §8(" + (int) uPercent + "%)",
                        "§7Points: §e" + points + "§7/§e" + maxPts + " §8(" + (int) pPercent + "%)",
                        "",
                        "§7Challenge achievements may be",
                        "§7completed a single time.",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIChallengeAchievements(category).open(player);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int unlocked = handler.getUnlockedCount(category, AchievementType.TIERED);
                int total = AchievementRegistry.getTierCount(category);
                int points = handler.getPoints(category, AchievementType.TIERED);
                int maxPts = AchievementRegistry.getTotalPoints(category, AchievementType.TIERED);
                double uPercent = total > 0 ? (unlocked * 100.0 / total) : 0;
                double pPercent = maxPts > 0 ? (points * 100.0 / maxPts) : 0;

                return ItemStackCreator.getStack(
                        "§aTiered Achievements",
                        Material.DIAMOND_BLOCK,
                        1,
                        "§8" + category.getDisplayName(),
                        "§7Unlocked: §b" + unlocked + "§7/§b" + total + " §8(" + (int) uPercent + "%)",
                        "§7Points: §e" + points + "§7/§e" + maxPts + " §8(" + (int) pPercent + "%)",
                        "",
                        "§7Tiered achievements are completed",
                        "§7over multiple tiers.",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUITieredAchievements(category).open(player);
            }
        });

        set(new GUIClickableItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Achievements");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIAchievementsMenu().open(player);
            }
        });

        set(new GUIItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getUsingGUIMaterial(
                        "§aTotal Completion",
                        category.getMaterial(),
                        1,
                        "§8" + category.getDisplayName(),
                        "§7Unlocked: §b" + totalUnlocked + "§7/§b" + totalCount + " §8(" + (int) unlockedPercent + "%)",
                        "§7Points: §e" + totalPoints + "§7/§e" + maxPoints + " §8(" + (int) pointsPercent + "%)"
                );
            }
        });

        set(new GUIClickableItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSeasonal Challenge Achievements",
                        Material.MAGMA_CREAM,
                        1,
                        "§7View challenge achievements for " + category.getDisplayName(),
                        "§7that are exclusive to seasonal",
                        "§7events.",
                        "",
                        "§8These achievements do not count",
                        "§8towards total game completion.",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISeasonalAchievements(category).open(player);
            }
        });

        updateItemStacks(getInventory(), player);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
