package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.achievement.AchievementCategory;
import net.swofty.type.generic.achievement.AchievementRegistry;
import net.swofty.type.generic.achievement.PlayerAchievementHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIAchievementsMenu extends HypixelInventoryGUI {

    public GUIAchievementsMenu() {
        super("Achievements", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        PlayerAchievementHandler handler = player.getAchievementHandler();

        set(createCategoryItem(1, AchievementCategory.GENERAL, Material.BOOK, handler));
        set(createCategoryItem(2, "Housing", Material.DARK_OAK_DOOR, handler, AchievementCategory.GENERAL));
        set(createCategoryItem(4, AchievementCategory.ARCADE, Material.SLIME_BALL, handler));
        set(createClassicGamesItem(5, handler));
        set(createSeasonalItem(6, handler));
        set(createLegacyItem(7, handler));

        set(createCategoryItem(3, AchievementCategory.SKYBLOCK, Material.PLAYER_HEAD, handler));

        set(createCategoryItem(19, AchievementCategory.TNT_GAMES, Material.TNT, handler));
        set(createCategoryItem(20, AchievementCategory.BLITZ_SG, Material.DIAMOND_SWORD, handler));
        set(createCategoryItem(21, AchievementCategory.MEGA_WALLS, Material.SOUL_SAND, handler));
        set(createCategoryItem(22, AchievementCategory.COPS_AND_CRIMS, Material.IRON_BARS, handler));
        set(createCategoryItem(23, AchievementCategory.UHC_CHAMPIONS, Material.GOLDEN_APPLE, handler));
        set(createCategoryItem(24, AchievementCategory.WARLORDS, Material.STONE_AXE, handler));
        set(createCategoryItem(25, AchievementCategory.SKYWARS, Material.ENDER_EYE, handler));

        set(createSmashHeroesItem(28, handler));
        set(createCategoryItem(29, AchievementCategory.SPEED_UHC, Material.GOLDEN_CARROT, handler));
        set(createCategoryItem(30, AchievementCategory.BEDWARS, Material.RED_BED, handler));
        set(createCategoryItem(31, AchievementCategory.MURDER_MYSTERY, Material.BOW, handler));
        set(createCategoryItem(32, AchievementCategory.BUILD_BATTLE, Material.CRAFTING_TABLE, handler));
        set(createCategoryItem(33, AchievementCategory.DUELS, Material.FISHING_ROD, handler));
        set(createCategoryItem(34, AchievementCategory.PIT, Material.DIRT, handler));

        set(createCategoryItem(40, AchievementCategory.WOOL_GAMES, Material.WHITE_WOOL, handler));

        set(new GUIItem(45) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int totalPoints = handler.getTotalPoints();
                boolean unlocked = totalPoints >= 10000;
                return ItemStackCreator.getStack(
                        unlocked ? "§6Gold Achievement Menu" : "§cGold Achievement Menu",
                        Material.CLOCK,
                        1,
                        "§7Changes achievement unlocks within",
                        "§7the menu to gold.",
                        "",
                        unlocked ? "§aUnlocked!" : "§cUnlocked with §b10,000 §cAchievement Points"
                );
            }
        });

        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To My Profile");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMyProfile().open(player);
            }
        });

        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int totalUnlocked = handler.getTotalUnlockedCount();
                int totalAchievements = AchievementRegistry.getTotalCount();
                int totalPoints = handler.getTotalPoints();
                int maxPoints = AchievementRegistry.getTotalMaxPoints();
                double unlockedPercent = totalAchievements > 0 ? (totalUnlocked * 100.0 / totalAchievements) : 0;
                double pointsPercent = maxPoints > 0 ? (totalPoints * 100.0 / maxPoints) : 0;

                String displayName = player.getFullDisplayName();

                return ItemStackCreator.getStack(
                        "§aHypixel Achievements Completion",
                        Material.DIAMOND,
                        1,
                        "§7Player: " + displayName,
                        "§7Unlocked: §b" + totalUnlocked + "§7/§b" + totalAchievements + " §8(" + (int) unlockedPercent + "%)",
                        "§7Points: §e" + totalPoints + "§7/§e" + maxPoints + " §8(" + (int) pointsPercent + "%)",
                        "",
                        "§7Legacy Unlocked: §b0",
                        "§7Legacy Points: §e0"
                );
            }
        });

        set(new GUIItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aAchievements Tracking §b[MVP§c+§b]",
                        Material.REPEATER,
                        1,
                        "§7Track achievements to access them",
                        "§7quickly in this menu and get instant",
                        "§7feedback of your progress."
                );
            }
        });

        set(new GUIClickableItem(51) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Achievement Rewards",
                        Material.GOLD_INGOT,
                        1,
                        "§7Unlock exclusive rewards for",
                        "§7achievement hunting efforts."
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§6Achievement Rewards coming soon!");
            }
        });

        set(new GUIItem(53) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSearch",
                        Material.OAK_SIGN,
                        1,
                        "§7Search for an achievement by name,",
                        "§7description or points value."
                );
            }
        });

        updateItemStacks(getInventory(), player);
    }

    private GUIClickableItem createCategoryItem(int slot, AchievementCategory category, Material material, PlayerAchievementHandler handler) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int unlocked = handler.getUnlockedCount(category);
                int total = AchievementRegistry.getByCategory(category).size();
                int points = handler.getTotalPoints(category);
                int maxPoints = AchievementRegistry.getTotalPoints(category);
                double unlockedPercent = total > 0 ? (unlocked * 100.0 / total) : 0;
                double pointsPercent = maxPoints > 0 ? (points * 100.0 / maxPoints) : 0;

                return ItemStackCreator.getStack(
                        "§a" + category.getDisplayName() + " Achievements",
                        material,
                        1,
                        "§7Unlocked: §b" + unlocked + "§7/§b" + total + " §8(" + (int) unlockedPercent + "%)",
                        "§7Points: §e" + points + "§7/§e" + maxPoints + " §8(" + (int) pointsPercent + "%)",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameAchievements(category).open(player);
            }
        };
    }

    private GUIClickableItem createCategoryItem(int slot, String name, Material material, PlayerAchievementHandler handler, AchievementCategory fallbackCategory) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int unlocked = handler.getUnlockedCount(fallbackCategory);
                int total = AchievementRegistry.getByCategory(fallbackCategory).size();
                int points = handler.getTotalPoints(fallbackCategory);
                int maxPoints = AchievementRegistry.getTotalPoints(fallbackCategory);
                double unlockedPercent = total > 0 ? (unlocked * 100.0 / total) : 0;
                double pointsPercent = maxPoints > 0 ? (points * 100.0 / maxPoints) : 0;

                return ItemStackCreator.getStack(
                        "§a" + name + " Achievements",
                        material,
                        1,
                        "§7Unlocked: §b" + unlocked + "§7/§b" + total + " §8(" + (int) unlockedPercent + "%)",
                        "§7Points: §e" + points + "§7/§e" + maxPoints + " §8(" + (int) pointsPercent + "%)",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameAchievements(fallbackCategory).open(player);
            }
        };
    }

    private GUIItem createClassicGamesItem(int slot, PlayerAchievementHandler handler) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aClassic Games Achievements",
                        Material.JUKEBOX,
                        1,
                        "§7Unlocked: §b0§7/§b376 §8(0%)",
                        "§7Points: §e0§7/§e4,065 §8(0%)",
                        "",
                        "§eClick to view achievements!"
                );
            }
        };
    }

    private GUIClickableItem createSeasonalItem(int slot, PlayerAchievementHandler handler) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§aSeasonal Achievements",
                        "f5612dc7b86d71afc1197301c15fd979e9f39e7b1f41d8f1ebdf8115576e2e",
                        1,
                        "§7Unlocked: §b0§7/§b251 §8(0%)",
                        "§7Points: §e0§7/§e2,500 §8(0%)",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.sendMessage("§6Seasonal achievements browser coming soon!");
            }
        };
    }

    private GUIItem createLegacyItem(int slot, PlayerAchievementHandler handler) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aLegacy Achievements",
                        Material.DIAMOND_BLOCK,
                        1,
                        "§7Unlocked: §b0",
                        "§7Points: §e0",
                        "",
                        "§7Due to events and games that are no",
                        "§7longer available, these achievements",
                        "§7cannot be earned anymore.",
                        "",
                        "§7Points from these achievements still",
                        "§7count towards achievement rewards,",
                        "§7but do not count towards",
                        "§7leaderboards.",
                        "",
                        "§eClick to view achievements!"
                );
            }
        };
    }

    private GUIClickableItem createSmashHeroesItem(int slot, PlayerAchievementHandler handler) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int unlocked = handler.getUnlockedCount(AchievementCategory.SMASH_HEROES);
                int total = AchievementRegistry.getByCategory(AchievementCategory.SMASH_HEROES).size();
                int points = handler.getTotalPoints(AchievementCategory.SMASH_HEROES);
                int maxPoints = AchievementRegistry.getTotalPoints(AchievementCategory.SMASH_HEROES);
                double unlockedPercent = total > 0 ? (unlocked * 100.0 / total) : 0;
                double pointsPercent = maxPoints > 0 ? (points * 100.0 / maxPoints) : 0;

                return ItemStackCreator.getStackHead(
                        "§aSmash Heroes Achievements",
                        "d29a9f57267ed342a13e3ad3a240c4c5af5a1a36ab2de0d6c2a31af0e3cdde",
                        1,
                        "§7Unlocked: §b" + unlocked + "§7/§b" + total + " §8(" + (int) unlockedPercent + "%)",
                        "§7Points: §e" + points + "§7/§e" + maxPoints + " §8(" + (int) pointsPercent + "%)",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameAchievements(AchievementCategory.SMASH_HEROES).open(player);
            }
        };
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
