package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointLong;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIViewPlayerProfile extends SkyBlockAbstractInventory {
    private final SkyBlockPlayer viewedPlayer;

    public GUIViewPlayerProfile(SkyBlockPlayer viewedPlayer) {
        super(InventoryType.CHEST_6_ROW);
        this.viewedPlayer = viewedPlayer;
        doAction(new SetTitleAction(Component.text(viewedPlayer.getUsername() + "'s Profile")));
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

        // Held Item
        attachItem(GUIItem.builder(2)
                .item(() -> !viewedPlayer.getItemInMainHand().isAir() ?
                        ItemStackCreator.getFromStack(viewedPlayer.getItemInMainHand()).build() :
                        ItemStackCreator.getStack("§7Empty Held Item Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .build());

        // Helmet
        attachItem(GUIItem.builder(11)
                .item(() -> !viewedPlayer.getHelmet().isAir() ?
                        ItemStackCreator.getFromStack(viewedPlayer.getHelmet()).build() :
                        ItemStackCreator.getStack("§7Empty Helmet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .build());

        // Chestplate
        attachItem(GUIItem.builder(20)
                .item(() -> !viewedPlayer.getChestplate().isAir() ?
                        ItemStackCreator.getFromStack(viewedPlayer.getChestplate()).build() :
                        ItemStackCreator.getStack("§7Empty Chestplate Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .build());

        // Leggings
        attachItem(GUIItem.builder(29)
                .item(() -> !viewedPlayer.getLeggings().isAir() ?
                        ItemStackCreator.getFromStack(viewedPlayer.getLeggings()).build() :
                        ItemStackCreator.getStack("§7Empty Leggings Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .build());

        // Boots
        attachItem(GUIItem.builder(38)
                .item(() -> !viewedPlayer.getBoots().isAir() ?
                        ItemStackCreator.getFromStack(viewedPlayer.getBoots()).build() :
                        ItemStackCreator.getStack("§7Empty Boots Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .build());

        // Pet
        attachItem(GUIItem.builder(47)
                .item(() -> {
                    if (viewedPlayer.getPetData().getEnabledPet() != null && !viewedPlayer.getPetData().getEnabledPet().getItemStack().isAir()) {
                        SkyBlockItem pet = viewedPlayer.getPetData().getEnabledPet();
                        return new NonPlayerItemUpdater(pet).getUpdatedItem().build();
                    }
                    return ItemStackCreator.getStack("§7Empty Pet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build();
                })
                .build());

        // Player Stats
        attachItem(GUIItem.builder(22)
                .item(() -> {
                    DataHandler dataHandler = viewedPlayer.getDataHandler();
                    String age = StringUtility.profileAge(System.currentTimeMillis() - dataHandler.get(DataHandler.Data.CREATED, DatapointLong.class).getValue());
                    List<String> lore = new ArrayList<>();
                    lore.add("§7 ");
                    lore.add("§7SkyBlock Level: " + viewedPlayer.getSkyBlockExperience().getLevel().getColor() + viewedPlayer.getSkyBlockExperience().getLevel());
                    lore.add("§7 ");
                    lore.add("§7Oldest Profile: §5" + age);
                    return ItemStackCreator.getStackHead(viewedPlayer.getShortenedDisplayName(),
                            PlayerSkin.fromUuid(viewedPlayer.getUuid().toString()), 1, lore).build();
                })
                .build());

        // Emblem
        attachItem(GUIItem.builder(31)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    DataHandler dataHandler = viewedPlayer.getDataHandler();
                    String name;
                    Material material;
                    if (dataHandler.get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getCurrentEmblem() != null) {
                        name = "§fSelected Emblem: " + dataHandler.get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getCurrentEmblem().toString();
                        material = dataHandler.get(DataHandler.Data.SKYBLOCK_EXPERIENCE, DatapointSkyBlockExperience.class).getValue().getEmblem().displayMaterial();
                        lore.add(" ");
                        lore.add("§eClick to view unlocked emblems!");
                    } else {
                        name = "§cNo Selected Emblem";
                        material = Material.BARRIER;
                        lore.add("§fThis player does not have any");
                        lore.add("§femblem selected.");
                        lore.add(" ");
                        lore.add("§eClick to view unlocked emblems!");
                    }
                    return ItemStackCreator.getStack(name, material, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());

        // Visit Island
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§aVisit Island", Material.FEATHER, 1, "§eClick to visit!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());

        // Trade Request
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStack("§aTrade Request", Material.EMERALD, 1, "§eSend a trade request!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());

        // Invite to Island
        attachItem(GUIItem.builder(24)
                .item(ItemStackCreator.getStack("§aInvite to Island", Material.POPPY, 1, "§eClick to invite!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());

        // Coop Request
        attachItem(GUIItem.builder(25)
                .item(ItemStackCreator.getStack("§aCo-op Request", Material.DIAMOND, 1, "§eSend a co-op request!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());

        // Personal Vault
        attachItem(GUIItem.builder(33)
                .item(ItemStackCreator.getStack("§aPersonal Vault", Material.ENDER_CHEST, 1, "§eClick to view!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());

        // Museum
        attachItem(GUIItem.builder(34)
                .item(() -> {
                    List<String> lore = new ArrayList<>();
                    DataHandler dataHandler = viewedPlayer.getDataHandler();
                    lore.add("§fProfile: §a" + dataHandler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
                    lore.add(" ");
                    lore.add("§fItems Donated: §bMuseum not there yet :)");
                    lore.add(" ");
                    lore.add("§eClick to visit!");
                    return ItemStackCreator.getStackHead(viewedPlayer.getUsername() + "'s Museum",
                            PlayerSkin.fromUuid(viewedPlayer.getUuid().toString()), 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cThis feature is not added yet.");
                    return true;
                })
                .build());
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {}
}