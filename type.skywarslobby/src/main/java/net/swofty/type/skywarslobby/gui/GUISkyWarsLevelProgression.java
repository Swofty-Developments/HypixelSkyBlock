package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.level.SkywarsLevelCategory;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;

import java.util.ArrayList;
import java.util.List;

public class GUISkyWarsLevelProgression extends HypixelInventoryGUI {
    private final int page;

    private static final int[][] PAGE_1_SLOTS = {
            // Level -> Slot mapping
            {1, 0}, {2, 1}, {3, 2}, {4, 3}, {5, 4}, {6, 5}, {7, 6}, {8, 7},  // Row 0: levels 1-8
            {9, 16},  // Row 1, right side: level 9
            {10, 25}, {11, 24}, {12, 23}, {13, 22}, {14, 21}, {15, 20}, {16, 19},  // Row 2: levels 10-16 (right to left)
            {17, 28},  // Row 3, left side: level 17
            {18, 37}, {19, 38}, {20, 39}, {21, 40}, {22, 41}, {23, 42}, {24, 43}, {25, 44}  // Row 4: levels 18-25
    };

    // Page 2: Levels 26-50
    private static final int[][] PAGE_2_SLOTS = {
            {43, 1}, {44, 2}, {45, 3}, {46, 4}, {47, 5}, {48, 6}, {49, 7}, {50, 8},  // Row 0: levels 43-50
            {42, 10},  // Row 1: level 42
            {41, 19}, {40, 20}, {39, 21}, {38, 22}, {37, 23}, {36, 24}, {35, 25},  // Row 2: levels 41-35
            {34, 34},  // Row 3: level 34
            {26, 36}, {27, 37}, {28, 38}, {29, 39}, {30, 40}, {31, 41}, {32, 42}, {33, 43}  // Row 4: levels 26-33
    };

    // Page 3: Levels 51-75
    private static final int[][] PAGE_3_SLOTS = {
            {51, 0}, {52, 1}, {53, 2}, {54, 3}, {55, 4}, {56, 5}, {57, 6}, {58, 7},  // Row 0: levels 51-58
            {59, 16},  // Row 1: level 59
            {66, 19}, {65, 20}, {64, 21}, {63, 22}, {62, 23}, {61, 24}, {60, 25},  // Row 2: levels 66-60
            {67, 28},  // Row 3: level 67
            {68, 37}, {69, 38}, {70, 39}, {71, 40}, {72, 41}, {73, 42}, {74, 43}, {75, 44}  // Row 4: levels 68-75
    };

    public GUISkyWarsLevelProgression() {
        this(1);
    }

    public GUISkyWarsLevelProgression(int page) {
        super("SkyWars Level Progression", InventoryType.CHEST_6_ROW);
        this.page = Math.max(1, Math.min(page, 3));
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
        long playerXP = handler != null ? handler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue() : 0;
        int playerLevel = SkywarsLevelRegistry.calculateLevel(playerXP);
        boolean hideLevel = handler != null && handler.get(SkywarsDataHandler.Data.HIDE_LEVEL, DatapointBoolean.class).getValue();

        // Get the slot mapping for current page
        int[][] slotMapping = getSlotMappingForPage(page);

        // Populate level items
        for (int[] mapping : slotMapping) {
            int level = mapping[0];
            int slot = mapping[1];

            SkywarsLevelCategory.SkywarsLevel levelData = SkywarsLevelRegistry.getLevel(level);
            if (levelData == null) continue;

            set(createLevelItem(slot, levelData, playerXP, playerLevel));
        }

        // Go Back button (slot 48)
        set(new GUIClickableItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGo Back",
                        Material.ARROW,
                        1,
                        "§7To SkyWars Menu"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUISkyWarsMenu().open(player);
            }
        });

        // Close button (slot 49)
        set(GUIClickableItem.getCloseItem(49));

        // Hide Level toggle (slot 50)
        set(new GUIClickableItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                if (hideLevel) {
                    return ItemStackCreator.getStack(
                            "§aShow Level",
                            Material.GRAY_DYE,
                            1,
                            "§7Toggles whether your SkyWars Level",
                            "§7and Emblem show next to your name.",
                            "",
                            "§eClick to show!"
                    );
                } else {
                    return ItemStackCreator.getStack(
                            "§cHide Level",
                            Material.LIME_DYE,
                            1,
                            "§7Toggles whether your SkyWars Level",
                            "§7and Emblem show next to your name.",
                            "",
                            "§eClick to hide!"
                    );
                }
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                if (handler == null) return;
                DatapointBoolean hideLevelDp = handler.get(SkywarsDataHandler.Data.HIDE_LEVEL, DatapointBoolean.class);
                hideLevelDp.setValue(!hideLevelDp.getValue());
                new GUISkyWarsLevelProgression(page).open(player);
            }
        });

        // Previous Page button (slot 45) - only show if not on first page
        if (page > 1) {
            set(new GUIClickableItem(45) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aPrevious Page",
                            Material.ARROW,
                            1,
                            "§ePage " + (page - 1),
                            "",
                            "§6Right-click to jump to the start"
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    boolean isRightClick = e.getClick() instanceof Click.Right;
                    if (isRightClick) {
                        new GUISkyWarsLevelProgression(1).open(player);
                    } else {
                        new GUISkyWarsLevelProgression(page - 1).open(player);
                    }
                }
            });
        }

        // Next Page button (slot 53) - only show if not on last page
        if (page < 3) {
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aNext Page",
                            Material.ARROW,
                            1,
                            "§ePage " + (page + 1),
                            "",
                            "§6Right-click to jump to the end"
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    boolean isRightClick = e.getClick() instanceof Click.Right;
                    if (isRightClick) {
                        new GUISkyWarsLevelProgression(3).open(player);
                    } else {
                        new GUISkyWarsLevelProgression(page + 1).open(player);
                    }
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    /**
     * Get the slot mapping array for a specific page
     */
    private int[][] getSlotMappingForPage(int page) {
        return switch (page) {
            case 1 -> PAGE_1_SLOTS;
            case 2 -> PAGE_2_SLOTS;
            case 3 -> PAGE_3_SLOTS;
            default -> PAGE_1_SLOTS;
        };
    }

    /**
     * Create a GUI item for a specific level
     */
    private GUIItem createLevelItem(int slot, SkywarsLevelCategory.SkywarsLevel levelData, long playerXP, int playerLevel) {
        return new GUIItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                int level = levelData.level();
                boolean isUnlocked = playerLevel >= level;
                boolean isCurrentLevel = playerLevel == level - 1; // Working towards this level
                boolean isPrestige = levelData.isPrestige();

                // Determine display color
                String nameColor;
                Material displayMaterial;

                if (isUnlocked) {
                    nameColor = "§a"; // Green for unlocked
                    displayMaterial = levelData.material();
                } else if (isCurrentLevel) {
                    nameColor = "§e"; // Yellow for current progress
                    displayMaterial = levelData.material();
                } else {
                    nameColor = "§c"; // Red for locked
                    displayMaterial = Material.RED_STAINED_GLASS_PANE;
                }

                // Build lore
                List<String> lore = new ArrayList<>();
                lore.add("§8" + levelData.getLevelType());
                lore.add("");

                // Show progress if not level 1
                if (level > 1) {
                    if (isUnlocked) {
                        // Show completed progress
                        long requirement = levelData.requirement();
                        lore.add("§7Progress: §b" + SkywarsLevelCategory.formatXPRequirement(requirement) +
                                "§7/§a" + SkywarsLevelCategory.formatXPRequirement(requirement));
                        lore.add("§8[§b" + "\u25A0".repeat(10) + "§8]");
                    } else if (isCurrentLevel) {
                        // Show current progress
                        long progressXP = SkywarsLevelRegistry.getXPIntoCurrentLevel(playerXP);
                        long requirement = levelData.requirement();
                        double progress = SkywarsLevelRegistry.getProgressToNextLevel(playerXP);
                        int filled = (int) (progress * 10);

                        lore.add("§7Progress: §b" + SkywarsLevelCategory.formatXPRequirement(progressXP) +
                                "§7/§a" + SkywarsLevelCategory.formatXPRequirement(requirement));
                        lore.add("§8[§b" + "\u25A0".repeat(filled) + "§7" + "\u25A0".repeat(10 - filled) + "§8]");
                    } else {
                        // Show empty progress
                        long requirement = levelData.requirement();
                        lore.add("§7Progress: §b0§7/§a" + SkywarsLevelCategory.formatXPRequirement(requirement));
                        lore.add("§8[§7" + "\u25A0".repeat(10) + "§8]");
                    }
                    lore.add("");
                }

                // Show rewards
                lore.add("§7Rewards:");
                for (SkywarsLevelCategory.Reward reward : levelData.rewards()) {
                    lore.add(reward.getDisplayLine());
                }

                // Show unlock status
                if (isUnlocked) {
                    lore.add("");
                    lore.add("§a§lUNLOCKED");
                }

                String displayName = nameColor + "SkyWars Level " + level;

                if (levelData.headTexture() != null && (isUnlocked || isCurrentLevel)) {
                    return ItemStackCreator.getStackHead(
                            displayName,
                            levelData.headTexture(),
                            1,
                            lore.toArray(new String[0])
                    );
                }

                return ItemStackCreator.getStack(
                        displayName,
                        displayMaterial,
                        1,
                        lore.toArray(new String[0])
                );
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
