package net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bestiary.GUIBestiary;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUISkillCategory extends SkyBlockAbstractInventory {
    private static final int[] displaySlots = {
            9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53
    };

    private final SkillCategories category;
    private final int page;

    public GUISkillCategory(SkillCategories category, int page) {
        super(InventoryType.CHEST_6_ROW);
        this.category = category;
        this.page = page;
        doAction(new SetTitleAction(Component.text(category.toString() + " Skill")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());
        setupBasicButtons(player);
        setupSkillHeader(player);
        setupRewardSlots(player);

        if (category == SkillCategories.COMBAT) {
            setupBestiaryButton();
        }
    }

    private void setupBasicButtons(SkyBlockPlayer player) {
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
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1, "§7To Skills Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkills());
                    return true;
                })
                .build());

        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        List<SkillCategory.SkillReward> rewards = List.of(category.asCategory().getRewards());

        // Next page button
        if (rewards.size() > (page + 1) * displaySlots.length) {
            attachItem(GUIItem.builder(50)
                    .item(ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1,
                            "§7Click to view the next page of rewards.").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUISkillCategory(category, page + 1));
                        return true;
                    })
                    .build());
        }

        // Previous page button
        if (page > 0) {
            attachItem(GUIItem.builder(48)
                    .item(ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1,
                            "§7Click to view the previous page of rewards.").build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUISkillCategory(category, page - 1));
                        return true;
                    })
                    .build());
        }
    }

    private void setupBestiaryButton() {
        attachItem(GUIItem.builder(39)
                .item(ItemStackCreator.getStack("§3Bestiary", Material.WRITTEN_BOOK, 1,
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
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBestiary());
                    return true;
                })
                .build());
    }

    private void setupSkillHeader(SkyBlockPlayer player) {
        DatapointSkills.PlayerSkills skills = player.getSkills();
        Integer nextLevel = skills.getNextLevel(category);

        attachItem(GUIItem.builder(0)
                .item(() -> {
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
                            category.asCategory().getDisplayIcon(), 1, lore).build();
                })
                .build());
    }

    private void setupRewardSlots(SkyBlockPlayer player) {
        DatapointSkills.PlayerSkills skills = player.getSkills();
        int level = skills.getCurrentLevel(category);
        List<SkillCategory.SkillReward> rewards = List.of(category.asCategory().getRewards());

        int index = 0;
        for (SkillCategory.SkillReward reward : rewards.subList(page * displaySlots.length,
                Math.min(rewards.size(), (page + 1) * displaySlots.length))) {
            if (index >= displaySlots.length) break;
            int slot = displaySlots[index];

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
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

                        return ItemStackCreator.getStack(colour + category + " Level " +
                                StringUtility.getAsRomanNumeral(reward.level()), icon, 1, lore).build();
                    })
                    .build());

            index++;
        }
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }
}