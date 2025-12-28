package net.swofty.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.achievement.*;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUITieredAchievements extends HypixelInventoryGUI {
    private static final int ACHIEVEMENTS_PER_PAGE = 7;
    private static final int[][] COLUMN_SLOTS = {
            {1, 10, 19, 28, 37},
            {2, 11, 20, 29, 38},
            {3, 12, 21, 30, 39},
            {4, 13, 22, 31, 40},
            {5, 14, 23, 32, 41},
            {6, 15, 24, 33, 42},
            {7, 16, 25, 34, 43}
    };

    private static final String UNLOCKED_HEAD = "9631597dce4e4051e8d5a543641966ab54fbf25a0ed6047f11e6140d88bf48f";
    private static final String LOCKED_HEAD = "967a2f218a6e6e38f2b545f6c17733f4ef9bbb288e75402949c052189ee";

    private final AchievementCategory category;
    private int page = 0;
    private SortMode sortMode = SortMode.A_TO_Z;

    private enum SortMode {
        A_TO_Z("A to Z"),
        Z_TO_A("Z to A");

        private final String display;

        SortMode(String display) {
            this.display = display;
        }
    }

    public GUITieredAchievements(AchievementCategory category) {
        super(category.getDisplayName() + " Tiered Achievements", InventoryType.CHEST_6_ROW);
        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        PlayerAchievementHandler handler = player.getAchievementHandler();

        List<AchievementDefinition> tieredAchievements = AchievementRegistry.getByCategory(category).stream()
                .filter(a -> a.getType() == AchievementType.TIERED)
                .toList();

        tieredAchievements = sortAchievements(tieredAchievements);

        int totalTiers = AchievementRegistry.getTierCount(category);
        int unlockedTiers = handler.getUnlockedCount(category, AchievementType.TIERED);
        int totalPoints = handler.getPoints(category, AchievementType.TIERED);
        int maxPoints = AchievementRegistry.getTotalPoints(category, AchievementType.TIERED);
        double unlockedPercent = totalTiers > 0 ? (unlockedTiers * 100.0 / totalTiers) : 0;
        double pointsPercent = maxPoints > 0 ? (totalPoints * 100.0 / maxPoints) : 0;

        int startIndex = page * ACHIEVEMENTS_PER_PAGE;
        int endIndex = Math.min(startIndex + ACHIEVEMENTS_PER_PAGE, tieredAchievements.size());

        for (int col = 0; col < ACHIEVEMENTS_PER_PAGE; col++) {
            int achievementIndex = startIndex + col;
            if (achievementIndex >= endIndex) break;

            AchievementDefinition achievement = tieredAchievements.get(achievementIndex);
            populateAchievementColumn(col, achievement, handler);
        }

        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To " + category.getDisplayName() + " Achievements");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGameAchievements(category).open(player);
            }
        });

        set(new GUIItem(49) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getUsingGUIMaterial(
                        "§aTiered Achievements",
                        category.getMaterial(),
                        1,
                        "§8" + category.getDisplayName(),
                        "§7Unlocked: §b" + unlockedTiers + "§7/§b" + totalTiers + " §8(" + (int) unlockedPercent + "%)",
                        "§7Points: §e" + totalPoints + "§7/§e" + maxPoints + " §8(" + (int) pointsPercent + "%)"
                );
            }
        });

        set(new GUIClickableItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGo to Challenge Achievements",
                        Material.DIAMOND,
                        1,
                        "§7Click to view " + category.getDisplayName() + " Challenge",
                        "§7Achievements."
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIChallengeAchievements(category).open(player);
            }
        });

        set(new GUIClickableItem(51) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String nextSort = sortMode == SortMode.A_TO_Z ? "Z to A" : "A to Z";
                return ItemStackCreator.getStack(
                        "§6Sorted by: §a" + sortMode.display,
                        Material.HOPPER,
                        1,
                        "§7Sorts by name from " + sortMode.display + ".",
                        "",
                        "§7Next sort: §a" + nextSort,
                        "§eLeft click to use!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                sortMode = sortMode == SortMode.A_TO_Z ? SortMode.Z_TO_A : SortMode.A_TO_Z;
                page = 0;
                open(player);
            }
        });

        int maxPages = getMaxPages(tieredAchievements.size());
        if (maxPages > 1) {
            if (page > 0) {
                set(new GUIClickableItem(45) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        return ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1);
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                        page--;
                        open(player);
                    }
                });
            }
            if (page < maxPages - 1) {
                set(new GUIClickableItem(53) {
                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        return ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1);
                    }

                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                        page++;
                        open(player);
                    }
                });
            }
        }

        updateItemStacks(getInventory(), player);
    }

    private List<AchievementDefinition> sortAchievements(List<AchievementDefinition> achievements) {
        List<AchievementDefinition> sorted = new ArrayList<>(achievements);
        Comparator<AchievementDefinition> comparator = Comparator.comparing(AchievementDefinition::getName);
        if (sortMode == SortMode.Z_TO_A) {
            comparator = comparator.reversed();
        }
        sorted.sort(comparator);
        return sorted;
    }

    private void populateAchievementColumn(int column, AchievementDefinition achievement, PlayerAchievementHandler handler) {
        List<AchievementTier> tiers = achievement.getTiers();
        if (tiers == null || tiers.isEmpty()) return;

        int currentTier = handler.getAchievementTier(achievement.getId());
        int currentProgress = handler.getProgress(achievement.getId());
        int[] slots = COLUMN_SLOTS[column];

        boolean isTracked = handler.isTracked(achievement.getId());
        boolean isFullyCompleted = handler.hasFullyCompletedAchievement(achievement.getId());

        for (int row = 0; row < 5; row++) {
            int tierNum = 5 - row;
            int slot = slots[row];

            if (tierNum > tiers.size()) {
                continue;
            }

            AchievementTier tier = tiers.get(tierNum - 1);
            boolean unlocked = currentTier >= tierNum;
            boolean isCurrent = currentTier == tierNum - 1 && !unlocked;

            set(createTierItem(slot, achievement, tier, tierNum, unlocked, isCurrent, currentProgress, row == 0, isTracked, isFullyCompleted));
        }
    }

    private GUIClickableItem createTierItem(int slot, AchievementDefinition achievement, AchievementTier tier,
                                             int tierNum, boolean unlocked, boolean isCurrent, int currentProgress,
                                             boolean isTopRow, boolean isTracked, boolean isFullyCompleted) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                String tierName = achievement.getName() + " " + toRoman(tierNum);
                String color = unlocked ? "§a" : "§c";

                List<String> lore = new ArrayList<>();
                lore.add("§f" + achievement.getDescription());
                lore.add("");

                if (unlocked) {
                    lore.add("§7Progress: §aDONE! §7(§a" + String.format("%,d", currentProgress) + "§7)");
                } else {
                    lore.add("§7Progress: §a" + String.format("%,d", currentProgress) + "§7/§a" + String.format("%,d", tier.getGoal()));
                }

                lore.add("§7Reward:");
                lore.add("§8+§e" + tier.getPoints() + " §7Achievement Points");
                lore.add("");

                if (unlocked) {
                    lore.add("§aTier unlocked!");
                } else {
                    lore.add("§cTier locked!");
                }

                lore.add("");
                String unlockPct = AchievementStatisticsService.getFormattedPercentage(achievement.getId(), tierNum);
                lore.add("§7Unlocked by §f" + unlockPct + "§7% of players!");

                if (!isFullyCompleted) {
                    lore.add("");
                    if (isTracked) {
                        lore.add("§6Currently tracking this achievement!");
                        lore.add("§eClick to stop tracking.");
                    } else {
                        lore.add("§7Track this achievement to gain progress.");
                        lore.add("§eClick to track!");
                    }
                }

                ItemStack.Builder builder;

                if (isTopRow) {
                    String texture = unlocked ? UNLOCKED_HEAD : LOCKED_HEAD;
                    builder = ItemStackCreator.getStackHead(
                            color + tierName,
                            texture,
                            1,
                            lore.toArray(new String[0])
                    );
                } else {
                    Material mat;
                    if (unlocked) {
                        mat = Material.LIGHT_BLUE_STAINED_GLASS_PANE;
                    } else if (isCurrent) {
                        mat = Material.YELLOW_STAINED_GLASS_PANE;
                    } else {
                        mat = Material.GRAY_STAINED_GLASS_PANE;
                    }

                    builder = ItemStackCreator.getStack(
                            color + tierName,
                            mat,
                            1,
                            lore.toArray(new String[0])
                    );
                }

                if (isTracked) {
                    builder = ItemStackCreator.enchant(builder);
                }

                return builder;
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (isFullyCompleted) {
                    player.sendMessage("§cThis achievement is already fully completed!");
                    return;
                }

                player.getAchievementHandler().toggleTracking(achievement.getId());
                open(player);
            }
        };
    }

    private String toRoman(int num) {
        return switch (num) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(num);
        };
    }

    private int getMaxPages(int totalAchievements) {
        return Math.max(1, (totalAchievements + ACHIEVEMENTS_PER_PAGE - 1) / ACHIEVEMENTS_PER_PAGE);
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
