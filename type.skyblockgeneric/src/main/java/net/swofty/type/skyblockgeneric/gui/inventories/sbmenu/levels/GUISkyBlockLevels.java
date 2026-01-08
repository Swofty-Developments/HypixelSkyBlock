package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards.GUILevelRewards;
import net.swofty.type.skyblockgeneric.levels.LevelsGuide;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockLevels extends HypixelInventoryGUI {
    public GUISkyBlockLevels() {
        super("SkyBlock Leveling", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.openView(new GUISkyBlockMenu());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack("§aGo Back",
                        Material.ARROW, 1, "§7To SkyBlock Menu");
            }
        });

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.sendMessage(player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT) ?
                        "§cSkyBlock Levels in Chat is now disabled!" :
                        "§aSkyBlock Levels in Chat is now enabled!");

                player.getToggles().set(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT,
                        !player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT));

                getInventory().setItemStack(50, getItem(player).build());
                getInventory().update();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§bSkyBlock Levels in Chat",
                        player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT)
                                ? Material.LIME_DYE : Material.GRAY_DYE,
                        1,
                        "§7View other players' SkyBlock Level",
                        "§7and their selected emblem in their",
                        "§7chat messages.",
                        " ",
                        player.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT)
                                ? "§a§lENABLED" : "§c§lDISABLED",
                        " ",
                        "§eClick to toggle!");
            }
        });

        set(new GUIClickableItem(34) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUILevelRewards().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>();
                lore.add("§7Unlock rewards for leveling up");
                lore.add("§7your SkyBlock Level.");
                lore.add(" ");
                lore.addAll(GUILevelRewards.getAsDisplay(GUILevelRewards.getUnlocked(player),
                        GUILevelRewards.getTotalAwards()));
                lore.add(" ");
                lore.add("§eClick to view rewards!");

                return ItemStackCreator.getStack("§aLeveling Rewards",
                        Material.CHEST, 1, lore);
            }
        });

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockLevelRequirement level = player.getSkyBlockExperience().getLevel();
                int completedChallenges = player.getSkyBlockExperience().getCompletedExperienceCauses().size();
                int totalChallenges = SkyBlockLevelCause.getAmountOfCauses();

                return ItemStackCreator.getStack("§aYour SkyBlock Level Ranking",
                        Material.PAINTING, 1,
                        "§8Classic Mode",
                        " ",
                        "§7Your level: " + level.getColor() + level,
                        "§7You have: §b" + Math.round(player.getSkyBlockExperience().getTotalXP()) + " XP",
                        " ",
                        "§7You have completed §3" + (new java.text.DecimalFormat("##.##").format((double) completedChallenges / totalChallenges * 100)) + "% §7of the total",
                        "§7SkyBlock XP Tasks.");
            }
        });
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUILevelsGuide(LevelsGuide.STARTER).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aSkyBlock Guide", Material.FILLED_MAP, 1,
                        "§7Your §6SkyBlock Guide §7tracks the",
                        "§7progress you have made through",
                        "§7SkyBlock.",
                        " ",
                        "§7Complete tasks within your current",
                        "§7game stage to increase your",
                        "§bSkyBlock Level §7and become a §dMaster",
                        "§7of SkyBlock!",
                        " ",
                        "§eClick to view!");
            }
        });
        set(new GUIClickableItem(43) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUIEmblems().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
                        "§7Add some spice by having an emblem",
                        "§7next to your name in chat and in tab!",
                        " ",
                        "§7Emblems are unlocked through various",
                        "§7activities such as leveling up",
                        "§7or completing achievements!",
                        " ",
                        "§7Emblems also show important data",
                        "§7associated with them in chat!",
                        " ",
                        "§eClick to view!");
            }
        });

        SkyBlockLevelRequirement currentLevel = ((SkyBlockPlayer) getPlayer()).getSkyBlockExperience().getLevel();
        List<SkyBlockLevelRequirement> levels = new ArrayList<>();
        levels.add(currentLevel);
        for (int i = 0; i < 5; i++) {
            if (currentLevel.getNextLevel() == null) {
                break;
            }
            levels.add(currentLevel.getNextLevel());
            currentLevel = currentLevel.getNextLevel();
        }

        int unlockedLevel = ((SkyBlockPlayer) getPlayer()).getSkyBlockExperience().getLevel().asInt();
        for (int i = 0; i < 5; i++) {
            if (levels.get(i) == null) {
                break;
            }

            SkyBlockLevelRequirement level = levels.get(i);
            set(new GUIClickableItem(19 + i) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUISkyBlockLevel(level).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    List<String> lore = new ArrayList<>();
                    Material material = level.isMilestone() ? Material.RED_STAINED_GLASS : Material.RED_STAINED_GLASS_PANE;

                    if (unlockedLevel == level.asInt()) {
                        lore.add("§8Your Level");
                        lore.add(" ");
                        material = level.isMilestone() ? Material.LIME_STAINED_GLASS : Material.LIME_STAINED_GLASS_PANE;
                    } else if (unlockedLevel + 1 == level.asInt()) {
                        lore.add("§8Next Level");
                        lore.add(" ");
                        material = level.isMilestone() ? Material.YELLOW_STAINED_GLASS : Material.YELLOW_STAINED_GLASS_PANE;
                    }

                    lore.add("§7Rewards:");
                    level.getUnlocks().forEach(unlock -> {
                        lore.addAll(unlock.getDisplay(player, level.asInt()));
                    });
                    lore.add(" ");
                    if (unlockedLevel == level.asInt()) {
                        lore.add("§a§lUNLOCKED");
                        lore.add(" ");
                    }
                    lore.add("§eClick to view rewards!");

                    return ItemStackCreator.getStack("§7Level " + level.asInt(),
                            material,
                            1, lore);
                }
            });
        }

        SkyBlockLevelRequirement currentLevelMilestone = ((SkyBlockPlayer) getPlayer()).getSkyBlockExperience().getLevel().getNextMilestoneLevel();
        if (currentLevelMilestone != null)
            set(new GUIClickableItem(30) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUISkyBlockLevel(currentLevelMilestone).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    List<String> lore = new ArrayList<>();
                    lore.add("§8Next Milestone Level");
                    lore.add(" ");
                    lore.add("§7Rewards:");
                    currentLevelMilestone.getUnlocks().forEach(unlock -> {
                        lore.addAll(unlock.getDisplay(player, currentLevelMilestone.asInt()));
                    });
                    lore.add(" ");
                    lore.add("§7XP Left to Gain: §b" + (currentLevelMilestone.getCumulativeExperience() - player.getSkyBlockExperience().getTotalXP())
                            + " XP §8(" + (int) (player.getSkyBlockExperience().getTotalXP() / currentLevelMilestone.getCumulativeExperience() * 100) + "%)");
                    lore.add(" ");
                    lore.add("§eClick to view rewards!");

                    return ItemStackCreator.getStack("§7Level " + currentLevelMilestone.asInt(),
                            Material.PURPLE_STAINED_GLASS_PANE,
                            1, lore);
                }
            });

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(net.minestom.server.event.inventory.InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
