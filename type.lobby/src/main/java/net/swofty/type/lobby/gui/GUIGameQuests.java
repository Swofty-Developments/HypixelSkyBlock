package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.achievement.AchievementCategory;
import net.swofty.type.generic.achievement.AchievementRegistry;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.quest.QuestData;
import net.swofty.type.generic.quest.QuestDefinition;
import net.swofty.type.generic.quest.QuestRegistry;
import net.swofty.type.generic.quest.QuestType;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIGameQuests extends HypixelInventoryGUI {
    private final AchievementCategory category;

    public GUIGameQuests(AchievementCategory category) {
        super(category.getDisplayName() + " Quests", InventoryType.CHEST_6_ROW);
        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        QuestData questData = player.getQuestHandler().getQuestData();

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getUsingGUIMaterial(
                        "§a" + category.getDisplayName() + " Quests & Challenges",
                        category.getMaterial(),
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing " + category.getDisplayName() + "."
                );
            }
        });

        populateDailyQuests(player, questData);
        populateWeeklyQuests(player, questData);
        populateSpecialDailyQuests(player, questData);
        populateChallengeQuests(player, questData);

        set(new GUIClickableItem(45) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int unlocked = player.getAchievementHandler().getUnlockedCount(category);
                int total = AchievementRegistry.getByCategory(category).size();
                int points = player.getAchievementHandler().getTotalPoints(category);
                int maxPoints = AchievementRegistry.getTotalPoints(category);
                int unlockedPercent = total > 0 ? (int) (unlocked * 100.0 / total) : 0;
                int pointsPercent = maxPoints > 0 ? (int) (points * 100.0 / maxPoints) : 0;

                return ItemStackCreator.getStack(
                        "§a" + category.getDisplayName() + " Achievements",
                        Material.DIAMOND,
                        1,
                        "§7Unlocked: §b" + unlocked + "§7/§b" + total + " §8(" + unlockedPercent + "%)",
                        "§7Points: §e" + points + "§7/§e" + maxPoints + " §8(" + pointsPercent + "%)",
                        "",
                        "§eClick to view achievements!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameAchievements(category).open(player);
            }
        });

        set(new GUIClickableItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGo Back",
                        Material.ARROW,
                        1,
                        "§7To Quests & Challenges"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIQuestsChallenges().open(player);
            }
        });

        set(new GUIClickableItem(53) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                boolean hasMvpPlus = player.getRank().isEqualOrHigherThan(Rank.MVP_PLUS);

                if (hasMvpPlus) {
                    boolean isEnabled = player.getToggles().get(DatapointToggles.Toggles.ToggleType.AUTO_ACCEPT_QUESTS);
                    if (isEnabled) {
                        return ItemStackCreator.getStack(
                                "§aAuto-Accept Quests: §aON",
                                Material.LIME_DYE,
                                1,
                                "§7Quests will be automatically",
                                "§7accepted whenever you join a",
                                "§7game lobby.",
                                "",
                                "§eClick to disable!"
                        );
                    } else {
                        return ItemStackCreator.getStack(
                                "§aAuto-Accept Quests: §cOFF",
                                Material.GRAY_DYE,
                                1,
                                "§7Click to automatically accept",
                                "§7quests whenever you join a",
                                "§7game lobby.",
                                "",
                                "§eClick to enable!"
                        );
                    }
                } else {
                    return ItemStackCreator.getStack(
                            "§aAuto-Accept Quests: §cOFF",
                            Material.GRAY_DYE,
                            1,
                            "§7Click to automatically accept",
                            "§7quests whenever you join a",
                            "§7game lobby.",
                            "",
                            "§7Requires §bMVP§c+"
                    );
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (player.getRank().isEqualOrHigherThan(Rank.MVP_PLUS)) {
                    boolean newValue = player.getToggles().inverse(DatapointToggles.Toggles.ToggleType.AUTO_ACCEPT_QUESTS);
                    if (newValue) {
                        player.sendMessage("§aAuto-Accept Quests enabled!");
                    } else {
                        player.sendMessage("§cAuto-Accept Quests disabled.");
                    }
                    open(player);
                } else {
                    player.sendMessage("§cThis feature requires MVP+!");
                }
            }
        });

        updateItemStacks(getInventory(), player);
    }

    private void populateDailyQuests(HypixelPlayer player, QuestData questData) {
        List<QuestDefinition> dailyQuests = QuestRegistry.getByCategory(category, QuestType.DAILY);
        int[] dailySlots = {9, 10, 11, 12};

        for (int i = 0; i < dailySlots.length; i++) {
            int slot = dailySlots[i];

            if (i < dailyQuests.size()) {
                QuestDefinition quest = dailyQuests.get(i);
                int progress = questData.getProgress(quest.getId());
                int goal = quest.getGoal();
                boolean completed = questData.isCompleted(quest.getId());
                boolean active = questData.isActive(quest.getId());

                set(new GUIClickableItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        List<String> lore = new ArrayList<>();
                        lore.add("§7" + quest.getDescription() + "§b (§6" + progress + "§b/§6" + goal + "§b)");
                        lore.add("");
                        lore.add("§7Rewards:");

                        if (quest.getReward() != null) {
                            if (quest.getReward().getHypixelExperience() > 0) {
                                lore.add("§8+§3" + quest.getReward().getHypixelExperience() + "§7 Hypixel Experience");
                            }
                            if (quest.getReward().getGameExperience() > 0) {
                                lore.add("§8+§b" + quest.getReward().getGameExperience() + "§7 " + category.getDisplayName() + " Experience");
                            }
                            if (quest.getReward().getCoins() > 0) {
                                lore.add("§8+§6" + quest.getReward().getCoins() + "§7 Coins");
                            }
                        }

                        lore.add("");
                        lore.add("§8§oDaily Quests can be completed once every");
                        lore.add("§8§oday.");
                        lore.add("");

                        if (completed) {
                            lore.add("§a§lCOMPLETED!");
                        } else if (active) {
                            lore.add("§aYou've already started this quest!");
                        } else {
                            lore.add("§eClick to start this quest!");
                        }

                        return ItemStackCreator.getStack(
                                "§aDaily Quest: " + quest.getName(),
                                Material.PAPER,
                                1,
                                lore.toArray(new String[0])
                        );
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        if (!questData.isActive(quest.getId()) && !questData.isCompleted(quest.getId())) {
                            p.getQuestHandler().startQuest(quest.getId());
                            p.sendMessage("§aQuest started: §e" + quest.getName());
                            open(p);
                        }
                    }
                });
            }
        }
    }

    private void populateWeeklyQuests(HypixelPlayer player, QuestData questData) {
        List<QuestDefinition> weeklyQuests = QuestRegistry.getByCategory(category, QuestType.WEEKLY);
        int[] weeklySlots = {14, 15, 16, 17};

        for (int i = 0; i < weeklySlots.length; i++) {
            int slot = weeklySlots[i];

            if (i < weeklyQuests.size()) {
                QuestDefinition quest = weeklyQuests.get(i);
                int progress = questData.getProgress(quest.getId());
                int goal = quest.getGoal();
                boolean completed = questData.isCompleted(quest.getId());
                boolean active = questData.isActive(quest.getId());

                set(new GUIClickableItem(slot) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        List<String> lore = new ArrayList<>();
                        lore.add("§7" + quest.getDescription() + "§b (§6" + progress + "§b/§6" + goal + "§b)");
                        lore.add("");
                        lore.add("§7Rewards:");

                        if (quest.getReward() != null) {
                            if (quest.getReward().getHypixelExperience() > 0) {
                                lore.add("§8+§3" + quest.getReward().getHypixelExperience() + "§7 Hypixel Experience");
                            }
                            if (quest.getReward().getGameExperience() > 0) {
                                lore.add("§8+§b" + quest.getReward().getGameExperience() + "§7 " + category.getDisplayName() + " Experience");
                            }
                            if (quest.getReward().getCoins() > 0) {
                                lore.add("§8+§6" + quest.getReward().getCoins() + "§7 Coins");
                            }
                        }

                        lore.add("");
                        lore.add("§8§oWeekly Quests can be completed once every");
                        lore.add("§8§oweek. Resets Thursday night.");
                        lore.add("");

                        if (completed) {
                            lore.add("§a§lCOMPLETED!");
                        } else if (active) {
                            lore.add("§aYou've already started this quest!");
                        } else {
                            lore.add("§eClick to start this quest!");
                        }

                        return ItemStackCreator.getStack(
                                "§aWeekly Quest: " + quest.getName(),
                                Material.PAPER,
                                1,
                                lore.toArray(new String[0])
                        );
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        if (!questData.isActive(quest.getId()) && !questData.isCompleted(quest.getId())) {
                            p.getQuestHandler().startQuest(quest.getId());
                            p.sendMessage("§aQuest started: §e" + quest.getName());
                            open(p);
                        }
                    }
                });
            }
        }
    }

    private void populateSpecialDailyQuests(HypixelPlayer player, QuestData questData) {
        List<QuestDefinition> specialDailyQuests = QuestRegistry.getByCategory(category, QuestType.SPECIAL_DAILY);
        int[] specialSlots = {22};

        for (int i = 0; i < specialSlots.length && i < specialDailyQuests.size(); i++) {
            int slot = specialSlots[i];
            QuestDefinition quest = specialDailyQuests.get(i);
            int progress = questData.getProgress(quest.getId());
            int goal = quest.getGoal();
            boolean completed = questData.isCompleted(quest.getId());
            boolean active = questData.isActive(quest.getId());

            set(new GUIClickableItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7" + quest.getDescription() + "§b (§6" + progress + "§b/§6" + goal + "§b)");
                    lore.add("");
                    lore.add("§7Rewards:");

                    if (quest.getReward() != null) {
                        if (quest.getReward().getHypixelExperience() > 0) {
                            lore.add("§8+§3" + String.format("%,d", quest.getReward().getHypixelExperience()) + "§7 Hypixel Experience");
                        }
                        if (quest.getReward().getGameExperience() > 0) {
                            lore.add("§8+§b" + quest.getReward().getGameExperience() + "§7 " + category.getDisplayName() + " Experience");
                        }
                        if (quest.getReward().getCoins() > 0) {
                            lore.add("§8+§6" + quest.getReward().getCoins() + "§7 Coins");
                        }
                    }

                    lore.add("");
                    lore.add("§8§oDaily Quests can be completed once every");
                    lore.add("§8§oday.");
                    lore.add("");

                    if (completed) {
                        lore.add("§a§lCOMPLETED!");
                    } else if (active) {
                        lore.add("§aYou've already started this quest!");
                    } else {
                        lore.add("§eClick to start this quest!");
                    }

                    if (quest.getHeadTexture() != null) {
                        return ItemStackCreator.getStackHead(
                                "§aSpecial Daily: " + quest.getName(),
                                quest.getHeadTexture(),
                                1,
                                lore.toArray(new String[0])
                        );
                    } else {
                        return ItemStackCreator.getStack(
                                "§aSpecial Daily: " + quest.getName(),
                                Material.PAPER,
                                1,
                                lore.toArray(new String[0])
                        );
                    }
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    if (!questData.isActive(quest.getId()) && !questData.isCompleted(quest.getId())) {
                        p.getQuestHandler().startQuest(quest.getId());
                        p.sendMessage("§aQuest started: §e" + quest.getName());
                        open(p);
                    }
                }
            });
        }
    }

    private void populateChallengeQuests(HypixelPlayer player, QuestData questData) {
        List<QuestDefinition> challengeQuests = QuestRegistry.getByCategory(category, QuestType.CHALLENGE);
        int[] challengeSlots = {38, 39, 41, 42};
        int challengesRemaining = questData.getRemainingChallenges();

        for (int i = 0; i < challengeSlots.length && i < challengeQuests.size(); i++) {
            int slot = challengeSlots[i];
            QuestDefinition quest = challengeQuests.get(i);

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    List<String> lore = new ArrayList<>();
                    lore.add("§7" + quest.getDescription());
                    lore.add("");

                    if (quest.getReward() != null && quest.getReward().getHypixelExperience() > 0) {
                        lore.add("§7Reward: §8+§3" + quest.getReward().getHypixelExperience() + "§7 Hypixel Experience");
                    }

                    lore.add("");
                    lore.add("§8§oYou can complete the same challenge");
                    lore.add("§8§omultiple times per day, but only");
                    lore.add("§8§oonce per game.");
                    lore.add("");
                    lore.add("§7Challenges remaining today: §a" + challengesRemaining);

                    return ItemStackCreator.getStack(
                            "§a" + quest.getName(),
                            Material.MAP,
                            1,
                            lore.toArray(new String[0])
                    );
                }
            });
        }
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
