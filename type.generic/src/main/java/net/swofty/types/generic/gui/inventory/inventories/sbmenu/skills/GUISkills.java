package net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;

public class GUISkills extends SkyBlockAbstractInventory {
    private final int[] displaySlots = {
            20, 21, 22, 23, 24,
            30, 32
    };

    public GUISkills() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Your Skills")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build());

        // Header
        attachItem(GUIItem.builder(4)
                .item(ItemStackCreator.getStack("§aYour Skills", Material.DIAMOND_SWORD, 1,
                        "§7View your Skill progression and",
                        "§7rewards.").build())
                .build());

        setupSkillItems(player);
    }

    private void setupSkillItems(SkyBlockPlayer player) {
        Thread.startVirtualThread(() -> {
            SkillCategories[] allCategories = SkillCategories.values();
            int index = 0;

            for (int slot : displaySlots) {
                final SkillCategories category = allCategories[index];
                final SkillCategory skillCategory = category.asCategory();

                attachItem(GUIItem.builder(slot)
                        .item(() -> {
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
                                    skillCategory.getDisplayIcon(), 1, lore).build();
                        })
                        .onClick((ctx, item) -> {
                            ctx.player().openInventory(new GUISkillCategory(category, 0));
                            return true;
                        })
                        .build());

                index++;
            }
        });
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
        // Empty implementation
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        // Empty implementation
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }
}