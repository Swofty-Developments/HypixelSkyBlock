package net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bestiary.GUIBestiary;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUISkillCategory extends SkyBlockInventoryGUI {
    private static final int[] displaySlots = {
            9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53
    };

    private final SkillCategories category;
    private final int page;
    public GUISkillCategory(SkillCategories category, int page) {
        super(category.toString() + " Skill", InventoryType.CHEST_6_ROW);

        this.category = category;
        this.page = page;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkills()));

        DatapointSkills.PlayerSkills skills = e.player().getSkills();
        int level = skills.getCurrentLevel(category);
        Integer nextLevel = skills.getNextLevel(category);

        if (category == SkillCategories.COMBAT) {
            set(new GUIClickableItem(39) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIBestiary().open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§3Bestiary", Material.WRITTEN_BOOK, 1,
                            "§7The Bestiary is a compendium of",
                            "§7mobs in SkyBlock. It contains detailed",
                            "§7information on loot drops, your mob",
                            "§7stats, and more!",
                            " ",
                            "§7Kill mobs within §aFamilies §7to progress",
                            "§7and earn §arewards§7, including §b✯ Magic",
                            "§bFind §7bonuses towards mobs in the",
                            "§7Family.",
                            " ",
                            "§c§lHERE PROGRESS BAR",
                            " ",
                            "§eClick to view!"
                            );
                }
            });
        }

        set(new GUIItem(0) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(category.asCategory().getDescription());

                lore.add(" ");

                if (nextLevel == null) {
                    lore.add("§cMAX LEVEL REACHED");
                } else {
                    player.getSkills().getDisplay(lore, category, category.asCategory().getReward(nextLevel).requirement(),
                            "§7Progress to Level " + StringUtility.getAsRomanNumeral(nextLevel) + ": ");
                }

                lore.add(" ");
                lore.add("§8Increase your " + category + " Level to");
                lore.add("§8unlock Perks, statistic bonuses, and");
                lore.add("§8more!");

                return ItemStackCreator.getStack("§a" + category + " Skill",
                        category.asCategory().getDisplayIcon(), 1, lore);
            }
        });

        List<SkillCategory.SkillReward> rewards = List.of(category.asCategory().getRewards());

        // Check if there is a future page, if there is, add a next page button
        if (rewards.size() > (page + 1) * displaySlots.length) {
            set(new GUIClickableItem(50) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUISkillCategory(category, page + 1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1, "§7Click to view the next page of rewards.");
                }
            });
        }
        // Check if there is a previous page, if there is, add a previous page button
        if (page > 0) {
            set(new GUIClickableItem(48) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUISkillCategory(category, page - 1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1, "§7Click to view the previous page of rewards.");
                }
            });
        }


        int index = 0;
        // Split into pages depending on side of displaySlots
        for (SkillCategory.SkillReward reward : rewards.subList(page * displaySlots.length, Math.min(rewards.size(), (page + 1) * displaySlots.length))) {
            if (index >= displaySlots.length) break;
            int slot = displaySlots[index];

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    List<String> lore = new ArrayList<>();
                    reward.getDisplay(lore);

                    Material icon = Material.RED_STAINED_GLASS_PANE;
                    String colour = "§c";

                    if (level >= reward.level()) {
                        icon = Material.LIME_STAINED_GLASS_PANE;
                        colour = "§a";

                        lore.add(" ");
                        lore.add("§a§lUNLOCKED");
                    } else if ((level + 1) == reward.level()) {
                        icon = Material.YELLOW_STAINED_GLASS_PANE;
                        colour = "§e";

                        lore.add(" ");
                        player.getSkills().getDisplay(lore, category, reward.requirement(), "§7Progress: ");
                    }

                    return ItemStackCreator.getStack(colour + category + " Level " + StringUtility.getAsRomanNumeral(reward.level()),
                            icon, 1, lore);
                }
            });

            index++;
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
