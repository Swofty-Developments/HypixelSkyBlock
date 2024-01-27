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
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUISkillCategory extends SkyBlockInventoryGUI {
    private static final int[] displaySlots = {
            9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 22, 13, 6, 7, 8, 17, 26, 35, 44, 53
    };

    private SkillCategories category;
    public GUISkillCategory(SkillCategories category) {
        super(category.toString() + " Skill", InventoryType.CHEST_6_ROW);

        this.category = category;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkills()));

        DatapointSkills.PlayerSkills skills = e.player().getSkills();
        int level = skills.getCurrentLevel(category);
        Integer nextLevel = skills.getNextLevel(category);

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

                return ItemStackCreator.getStack("§a" + category.toString() + " Skill",
                        category.asCategory().getDisplayIcon(), 1, lore);
            }
        });

        int index = 0;
        for (SkillCategory.SkillReward reward : category.asCategory().getRewards()) {
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
