package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.experience.LevelReward;
import net.swofty.type.generic.experience.LevelRewardRegistry;
import net.swofty.type.generic.experience.PlayerExperienceHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIHypixelLeveling extends HypixelInventoryGUI {
    private static final int LEVELS_PER_PAGE = 25;
    private static final int[] REWARD_SLOTS = {
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            19, 20, 21, 22, 23, 24, 25
    };

    private static final double[] MULTIPLIERS = {1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
    private static final int[] MULTIPLIER_LEVELS = {10, 25, 50, 75, 100, 125, 150, 250};

    private int page = 0;

    public GUIHypixelLeveling() {
        super("Hypixel Leveling", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        PlayerExperienceHandler xpHandler = player.getExperienceHandler();

        int currentLevel = xpHandler.getLevel();
        double progress = xpHandler.getProgressToNextLevel();
        long xpNeeded = xpHandler.getXPForNextLevel() - xpHandler.getXPInCurrentLevel();

        populateLevelRewards(currentLevel);

        if (page > 0) {
            set(new GUIClickableItem(18) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack("§aPage " + page, Material.ARROW, 1);
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    page--;
                    open(player);
                }
            });
        }

        if (page < getMaxPages() - 1) {
            set(new GUIClickableItem(26) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack("§aPage " + (page + 2), Material.ARROW, 1);
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    page++;
                    open(player);
                }
            });
        }

        for (int i = 0; i < MULTIPLIERS.length; i++) {
            int slot = 36 + i;
            double mult = MULTIPLIERS[i];
            int reqLevel = MULTIPLIER_LEVELS[i];
            boolean unlocked = currentLevel >= reqLevel;

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§6" + mult + "x §aCoin Multiplier",
                            Material.GOLD_BLOCK,
                            1,
                            "",
                            "§7Increases the amount of coins you",
                            "§7earn when playing games.",
                            "",
                            "§8§oAutomatically unlocks upon reaching",
                            "§8§othe required level.",
                            "",
                            unlocked ? "§aUnlocked!" : "§cRequires Level " + reqLevel
                    );
                }
            });
        }

        set(new GUIClickableItem(44) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                boolean canAccess = currentLevel >= 100;
                return ItemStackCreator.getStack(
                        "§6Veteran Rewards",
                        Material.BEACON,
                        1,
                        "§7Rewards for the most dedicated",
                        "§7players!",
                        "",
                        canAccess ? "§eClick to view!" : "§cYou must be Hypixel Level 100 or",
                        canAccess ? "" : "§chigher to access this menu!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (currentLevel >= 100) {
                    player.sendMessage("§6Veteran Rewards coming soon!");
                } else {
                    player.sendMessage("§cYou must be Hypixel Level 100 or higher!");
                }
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
                String progressBar = createProgressBar(progress, 40);
                int progressPercent = (int) (progress * 100);

                return ItemStackCreator.getStack(
                        "§aHypixel Leveling",
                        Material.BREWING_STAND,
                        1,
                        "§7Playing games and completing quests",
                        "§7will reward you with §3Hypixel",
                        "§3Experience§7, which is required to",
                        "§7level up and acquire new perks and",
                        "§7rewards!",
                        "",
                        "§3Hypixel Level §a" + currentLevel + " §3" + progressBar + " §3" + progressPercent + "%",
                        "",
                        "§7Experience until next level: §3" + StringUtility.commaify(xpNeeded)
                );
            }
        });

        set(new GUIClickableItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aQuest Log",
                        Material.ENCHANTED_BOOK,
                        1,
                        "§7Completing quests will reward you",
                        "§7with §6Coins§7, §3Hypixel Experience §7and",
                        "§7more!",
                        "",
                        "§7Talk to §bQuest Masters §7located in",
                        "§7game lobbies to accept quests.",
                        "",
                        "§eClick to view quest progress!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIQuestsChallenges().open(player);
            }
        });

        updateItemStacks(getInventory(), player);
    }

    private void populateLevelRewards(int playerLevel) {
        int startLevel = page * LEVELS_PER_PAGE + 1;

        for (int i = 0; i < REWARD_SLOTS.length; i++) {
            int level = startLevel + i;
            int slot = REWARD_SLOTS[i];

            set(createLevelRewardItem(slot, level, playerLevel));
        }
    }

    private GUIItem createLevelRewardItem(int slot, int level, int playerLevel) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                boolean claimed = playerLevel >= level;
                LevelReward reward = LevelRewardRegistry.get(level);

                String nameColor = claimed ? "§c" : "§a";

                List<String> lore = new ArrayList<>();
                lore.add("");

                if (reward != null && reward.hasAnyReward()) {
                    if (reward.hasCoins()) {
                        lore.add("§8+§6" + StringUtility.commaify(reward.getCoins()) + " §7Arcade Coins");
                        lore.add("");
                        lore.add("§8§oArcade Coins can be exchanged for");
                        lore.add("§8§oother game coins inside the Arcade");
                        lore.add("§8§oLobby.");
                    }
                    if (reward.hasDust()) {
                        if (reward.getMysteryDust() > 0) {
                            lore.add("§8+§b" + reward.getMysteryDust() + " Mystery Dust");
                        }
                        if (reward.getDust() > 0) {
                            lore.add("§8+§b" + reward.getDust() + " Dust");
                        }
                    }
                    if (reward.hasBooster()) {
                        lore.add("§8+§72x §62.0x §7Personal Coin Booster");
                        lore.add("§7(§b" + reward.getBoosterDurationDisplay() + "§7)");
                        lore.add("");
                        lore.add("§8§oTo activate a coin booster, go to");
                        lore.add("§aMy Profile > Coin Boosters §8§oor type");
                        lore.add("§b§o/booster");
                    }
                    if (reward.hasSpecialRewards()) {
                        for (String special : reward.getSpecialRewards()) {
                            lore.add("§8+§7" + formatSpecialReward(special));
                            lore.add("");
                            lore.add("§8§oType §b§o/rankcolor §8§oto change the");
                            lore.add("§8§ocolor and view all options.");
                        }
                    }
                } else {
                    int coins = 40000 + (level * 1000);
                    lore.add("§8+§6" + StringUtility.commaify(coins) + " §7Arcade Coins");
                    lore.add("");
                    lore.add("§8§oArcade Coins can be exchanged for");
                    lore.add("§8§oother game coins inside the Arcade");
                    lore.add("§8§oLobby.");
                }

                lore.add("");
                if (claimed) {
                    lore.add("§aYou have already claimed this reward!");
                } else {
                    lore.add("§eReach Level " + level + " to claim!");
                }

                return ItemStackCreator.getStack(
                        nameColor + "Hypixel Level Reward " + level,
                        claimed ? Material.MINECART : Material.CHEST_MINECART,
                        1,
                        lore.toArray(new String[0])
                );
            }
        };
    }

    private String formatSpecialReward(String reward) {
        return switch (reward) {
            case "rankcolor_yellow" -> "Yellow §e+ §7option for §b[MVP§c+§b]";
            case "rankcolor_light_purple" -> "Light Purple §d+ §7option for §b[MVP§c+§b]";
            case "rankcolor_white" -> "White §f+ §7option for §b[MVP§c+§b]";
            case "rankcolor_aqua" -> "Aqua §b+ §7option for §b[MVP§c+§b]";
            case "rankcolor_dark_green" -> "Dark Green §2+ §7option for §b[MVP§c+§b]";
            case "rankcolor_dark_aqua" -> "Dark Aqua §3+ §7option for §b[MVP§c+§b]";
            case "rankcolor_dark_red" -> "Dark Red §4+ §7option for §b[MVP§c+§b]";
            case "rankcolor_dark_purple" -> "Dark Purple §5+ §7option for §b[MVP§c+§b]";
            case "rankcolor_gold" -> "Gold §6+ §7option for §b[MVP§c+§b]";
            case "rankcolor_gray" -> "Gray §7+ §7option for §b[MVP§c+§b]";
            case "rankcolor_dark_gray" -> "Dark Gray §8+ §7option for §b[MVP§c+§b]";
            case "rankcolor_blue" -> "Blue §9+ §7option for §b[MVP§c+§b]";
            case "rankcolor_green" -> "Green §a+ §7option for §b[MVP§c+§b]";
            case "rankcolor_red" -> "Red §c+ §7option for §b[MVP§c+§b]";
            case "rankcolor_black" -> "Black §0+ §7option for §b[MVP§c+§b]";
            case "multiplier_1.5x" -> "1.5x Coin Multiplier";
            case "multiplier_2.0x" -> "2.0x Coin Multiplier";
            case "multiplier_2.5x" -> "2.5x Coin Multiplier";
            case "multiplier_3.0x" -> "3.0x Coin Multiplier";
            case "multiplier_3.5x" -> "3.5x Coin Multiplier";
            case "multiplier_4.0x" -> "4.0x Coin Multiplier";
            case "multiplier_4.5x" -> "4.5x Coin Multiplier";
            case "multiplier_5.0x" -> "5.0x Coin Multiplier";
            case "veteran_status" -> "Veteran Status";
            default -> reward;
        };
    }

    private String createProgressBar(double progress, int length) {
        int filled = (int) (progress * length);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < length; i++) {
            bar.append(i < filled ? "|" : "§8|");
        }
        return bar.toString();
    }

    private int getMaxPages() {
        int maxLevel = Math.max(LevelRewardRegistry.getMaxRewardLevel(), 250);
        return (maxLevel + LEVELS_PER_PAGE - 1) / LEVELS_PER_PAGE;
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
