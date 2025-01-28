package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags.GUIYourBags;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection.GUICollections;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.fasttravel.GUIFastTravel;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles.GUIProfileManagement;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.questlog.GUIMissionLog;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipeBook;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills.GUISkills;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.storage.GUIStorage;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockMenu extends SkyBlockAbstractInventory {
    public GUISkyBlockMenu() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("SkyBlock Menu")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Profile button
        attachItem(GUIItem.builder(13)
                .item(() -> {
                    PlayerStatistics statistics = owner.getStatistics();
                    List<String> lore = new ArrayList<>(List.of("§7View your equipment, stats, and more!", "§e "));
                    List<String> stats = new ArrayList<>(List.of("Health", "Defense", "Speed", "Strength", "Intelligence",
                            "Crit Chance", "Crit Damage", "Swing Range"
                    ));

                    statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                        if (!value.equals(statistic.getBaseAdditiveValue()) || stats.contains(statistic.getDisplayName())) {
                            lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                    StringUtility.toNormalCase(statistic.name()) + " §f" +
                                    StringUtility.decimalify(value, 2) + statistic.getSuffix());
                        }
                    });

                    lore.add("§e ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStackHead("§aYour SkyBlock Profile",
                            PlayerSkin.fromUuid(owner.getUuid().toString()), 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockProfile());
                    return true;
                })
                .build());

        // SkyBlock Leveling
        attachItem(GUIItem.builder(22)
                .item(() -> {
                    SkyBlockLevelRequirement levelRequirement = owner.getSkyBlockExperience().getLevel();
                    SkyBlockLevelRequirement nextLevel = levelRequirement.getNextLevel();

                    return ItemStackCreator.getStackHead("§aSkyBlock Leveling",
                            "3255327dd8e90afad681a19231665bea2bd06065a09d77ac1408837f9e0b242", 1,
                            "§7Your SkyBlock Level: §8[" + levelRequirement.getColor() + levelRequirement + "§8]",
                            " ",
                            "§7Determine how far you've",
                            "§7progressed in SkyBlock and earn",
                            "§7rewards from completing unique",
                            "§7tasks.",
                            " ",
                            "§7Progress to Level " + (nextLevel == null ? "§cMAX" : nextLevel),
                            owner.getSkyBlockExperience().getNextLevelDisplay(),
                            " ",
                            "§eClick to view!").build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockLevels());
                    return true;
                })
                .build());

        // Bags
        attachItem(GUIItem.builder(29)
                .item(ItemStackCreator.getStackHead("§aYour Bags",
                        "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff", 1,
                        "§7Different bags allow you to store",
                        "§7many different items inside!",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIYourBags());
                    return true;
                })
                .build());

        // Pets
        attachItem(GUIItem.builder(30)
                .item(() -> ItemStackCreator.getStack("§aPets", Material.BONE, 1,
                        "§7View and manage all of your",
                        "§7Pets.",
                        " ",
                        "§7Level up your pets faster by",
                        "§7gaining XP in their favourite",
                        "§7skill!",
                        " ",
                        "§7Selected pet: " + (owner.getPetData().getEnabledPet() == null ? "§cNone" :
                                owner.getPetData().getEnabledPet().getDisplayName()),
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIInventoryPets());
                    return true;
                })
                .build());

        // Recipe Book
        attachItem(GUIItem.builder(21)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(
                            "§7Through your adventure, you will",
                            "§7unlock recipes for all kinds of",
                            "§7special items! You can view how to",
                            "§7craft these items here.",
                            " "
                    ));

                    SkyBlockRecipe.getMissionDisplay(lore, owner.getUuid());

                    lore.add(" ");
                    lore.add("§eClick to view!");
                    return ItemStackCreator.getStack("§aRecipe Book", Material.BOOK, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRecipeBook());
                    return true;
                })
                .build());

        // Storage
        attachItem(GUIItem.builder(25)
                .item(ItemStackCreator.getStack("§aStorage", Material.CHEST, 1,
                        "§7Store global items that you",
                        "§7want to access at any time",
                        "§7from anywhere here.",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIStorage());
                    return true;
                })
                .build());

        // Quest Log
        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStack("§aQuest Log", Material.WRITABLE_BOOK, 1,
                        "§7View your active quests, progress",
                        "§7and rewards.",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIMissionLog());
                    return true;
                })
                .build());

        // Skills
        attachItem(GUIItem.builder(19)
                .item(ItemStackCreator.getStack("§aYour Skills", Material.DIAMOND_SWORD, 1,
                        "§7View your Skill progression and",
                        "§7rewards.",
                        " ",
                        "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkills());
                    return true;
                })
                .build());

        // Collections
        attachItem(GUIItem.builder(20)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(
                            "§7View all of the items available in",
                            "§7SkyBlock. Collect more of an item to",
                            "§7unlock rewards on your way to",
                            "§7becoming a master of SkyBlock!",
                            " "
                    ));

                    owner.getCollection().getDisplay(lore);

                    lore.add(" ");
                    lore.add("§eClick to view!");
                    return ItemStackCreator.getStack("§aCollections", Material.PAINTING, 1,
                            lore.toArray(new String[0])).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUICollections());
                    return true;
                })
                .build());

        // Crafting Table
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aCrafting Table", Material.CRAFTING_TABLE, 1,
                        "§7Opens the crafting grid.",
                        " ",
                        "§eClick to open!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUICrafting());
                    return true;
                })
                .build());

        // Fast Travel
        attachItem(GUIItem.builder(47)
                .item(ItemStackCreator.getStackHead("§bFast Travel",
                        "f151cffdaf303673531a7651b36637cad912ba485643158e548d59b2ead5011", 1,
                        "§7Teleport to islands you've already",
                        "§7visited.",
                        " ",
                        "§8Right-click to warp home!",
                        "§eClick to pick location!").build())
                .onClick((ctx, item) -> {
                    if (ctx.clickType().equals(ClickType.RIGHT_CLICK)) {
                        ctx.player().closeInventory();
                        ctx.player().sendTo(ServerType.ISLAND);
                        return true;
                    }

                    ctx.player().openInventory(new GUIFastTravel());
                    return true;
                })
                .build());

        // Profile Management
        attachItem(GUIItem.builder(48)
                .item(() -> ItemStackCreator.getStack("§aProfile Management", Material.NAME_TAG, 1,
                        "§7You can have multiple SkyBlock",
                        "§7profiles at the same time.",
                        " ",
                        "§7Each profile has its own island,",
                        "§7inventory, quest log...",
                        " ",
                        "§7Profiles: §e" + owner.getProfiles().getProfiles().size() + "§6/§e4",
                        " ",
                        "§eClick to manage!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileManagement());
                    return true;
                })
                .build());
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
    }
}