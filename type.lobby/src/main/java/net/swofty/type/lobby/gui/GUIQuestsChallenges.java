package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.achievement.AchievementCategory;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIQuestsChallenges extends HypixelInventoryGUI {

    public GUIQuestsChallenges() {
        super("Quests & Challenges", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();

        set(new GUIClickableItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aCops and Crims Quests & Challenges",
                        Material.IRON_BARS,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Cops and",
                        "§7Crims.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.COPS_AND_CRIMS).open(player);
            }
        });

        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aBlitz SG Quests & Challenges",
                        Material.DIAMOND_SWORD,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Blitz SG.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.BLITZ_SG).open(player);
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aThe Walls Quests & Challenges",
                        Material.SAND,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing The Walls.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.THE_WALLS).open(player);
            }
        });

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aPaintball Warfare Quests & Challenges",
                        Material.SNOWBALL,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Paintball",
                        "§7Warfare.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.PAINTBALL).open(player);
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aVampireZ Quests & Challenges",
                        Material.WITHER_SKELETON_SKULL,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing VampireZ.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.VAMPIREZ).open(player);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aArcade Games Quests & Challenges",
                        Material.SLIME_BALL,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Arcade",
                        "§7Games.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.ARCADE).open(player);
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aQuakecraft Quests & Challenges",
                        Material.FIREWORK_ROCKET,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing",
                        "§7Quakecraft.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.QUAKECRAFT).open(player);
            }
        });

        set(new GUIClickableItem(19) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMega Walls Quests & Challenges",
                        Material.SOUL_SAND,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Mega",
                        "§7Walls.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.MEGA_WALLS).open(player);
            }
        });

        set(new GUIClickableItem(20) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aThe TNT Games Quests & Challenges",
                        Material.TNT,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing The TNT",
                        "§7Games.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.TNT_GAMES).open(player);
            }
        });

        set(new GUIClickableItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aArena Brawl Quests & Challenges",
                        Material.BLAZE_POWDER,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Arena",
                        "§7Brawl.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.ARENA_BRAWL).open(player);
            }
        });

        set(new GUIClickableItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aUHC Champions Quests & Challenges",
                        Material.GOLDEN_APPLE,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing UHC",
                        "§7Champions.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.UHC_CHAMPIONS).open(player);
            }
        });

        set(new GUIClickableItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aWarlords Quests & Challenges",
                        Material.STONE_AXE,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Warlords.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.WARLORDS).open(player);
            }
        });

        set(new GUIClickableItem(24) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSkyWars Quests & Challenges",
                        Material.ENDER_EYE,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing SkyWars.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.SKYWARS).open(player);
            }
        });

        set(new GUIClickableItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aTurbo Kart Racers Quests & Challenges",
                        Material.MINECART,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Turbo",
                        "§7Kart Racers.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.TURBO_KART_RACERS).open(player);
            }
        });

        set(new GUIClickableItem(28) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§aSmash Heroes Quests & Challenges",
                        "d29a9f57267ed342a13e3ad3a240c4c5af5a1a36ab2de0d6c2a31af0e3cdde",
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Smash",
                        "§7Heroes.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.SMASH_HEROES).open(player);
            }
        });

        set(new GUIClickableItem(29) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aSpeed UHC Quests & Challenges",
                        Material.GOLDEN_CARROT,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Speed",
                        "§7UHC.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.SPEED_UHC).open(player);
            }
        });

        set(new GUIClickableItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aBed Wars Quests & Challenges",
                        Material.RED_BED,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Bed Wars.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.BEDWARS).open(player);
            }
        });

        set(new GUIClickableItem(31) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMurder Mystery Quests & Challenges",
                        Material.BOW,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Murder",
                        "§7Mystery.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.MURDER_MYSTERY).open(player);
            }
        });

        set(new GUIClickableItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aBuild Battle Quests & Challenges",
                        Material.CRAFTING_TABLE,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Build",
                        "§7Battle.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.BUILD_BATTLE).open(player);
            }
        });

        set(new GUIClickableItem(33) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aDuels Quests & Challenges",
                        Material.FISHING_ROD,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Duels.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.DUELS).open(player);
            }
        });

        set(new GUIClickableItem(34) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aPit Quests & Challenges",
                        Material.DIRT,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Pit.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.PIT).open(player);
            }
        });

        set(new GUIClickableItem(37) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aWool Games Quests & Challenges",
                        Material.WHITE_WOOL,
                        1,
                        "§7View all available quests and challenges",
                        "§7that you can complete by playing Wool",
                        "§7Games.",
                        "",
                        "§eClick to view!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameQuests(AchievementCategory.WOOL_GAMES).open(player);
            }
        });

        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGo Back",
                        Material.ARROW,
                        1,
                        "§7To My Profile"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMyProfile().open(player);
            }
        });

        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int challengesCompleted = player.getQuestHandler().getQuestData().getDailyChallengesCompleted();
                return ItemStackCreator.getStack(
                        "§aQuests & Challenges",
                        Material.ENCHANTED_BOOK,
                        1,
                        "§7Completing quests and challenges",
                        "§7will reward you with §6Coins§7, §3Hypixel",
                        "§3Experience§7 and more!",
                        "",
                        "§7You can complete a maximum of §a15 ",
                        "§7challenges every day.",
                        "",
                        "§7Challenges completed today: §a" + challengesCompleted,
                        "",
                        "§eClick to view Quests & Challenges."
                );
            }
        });

        set(new GUIClickableItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aHypixel Leveling",
                        Material.BREWING_STAND,
                        1,
                        "§7Playing games and completing quests",
                        "§7will reward you with §3Hypixel",
                        "§3Experience§7, which is required to",
                        "§7level up and acquire new perks and",
                        "§7rewards!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIHypixelLeveling().open(player);
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

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
