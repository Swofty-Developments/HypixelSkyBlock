package net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
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

public class GUISkills extends SkyBlockInventoryGUI {
    private final int[] displaySlots = {
            20, 21, 22, 23, 24,
            30, 32
    };

    public GUISkills() {
        super("Your Skills", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        SkillCategories[] allCategories = SkillCategories.values();

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aYour Skills", Material.DIAMOND_SWORD, 1,
                        "§7View your Skill progression and",
                        "§7rewards.");
            }
        });
        updateItemStacks(getInventory(), getPlayer());

        Thread.startVirtualThread(() -> {
            int index = 0;
            for (int slot : displaySlots) {
                SkillCategories category = allCategories[index];
                SkillCategory skillCategory = category.asCategory();

                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        new GUISkillCategory(category, 0).open(player);
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        ArrayList<String> lore = new ArrayList<>(skillCategory.getDescription());
                        lore.add(" ");

                        Integer nextLevel = player.getSkills().getNextLevel(category);

                        if (nextLevel != null) {
                            player.getSkills().getDisplay(lore, category, skillCategory.getRewards()[nextLevel - 1].requirement(),
                                    "§7Progress to Level " + StringUtility.getAsRomanNumeral(nextLevel) + ": ");
                            lore.add(" ");

                            SkillCategory.SkillReward[] rewards = skillCategory.getRewards();
                            SkillCategory.SkillReward reward = rewards[nextLevel - 1];

                            reward.getDisplay(lore);
                        } else {
                            lore.add("§cMax Level Reached!");
                        }

                        lore.add(" ");
                        lore.add("§eClick to view!");

                        return ItemStackCreator.getStack(
                                "§a" + skillCategory.getName() + " " +
                                        StringUtility.getAsRomanNumeral(player.getSkills().getCurrentLevel(category)),
                                skillCategory.getDisplayIcon(), 1, lore);
                    }
                });

                updateItemStacks(getInventory(), getPlayer());
                index++;
            }
        });
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
