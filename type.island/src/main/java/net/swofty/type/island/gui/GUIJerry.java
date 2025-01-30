package net.swofty.type.island.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIJerry extends SkyBlockAbstractInventory {
    public GUIJerry() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Jerry The Assistant")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Patch Notes button
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§aPatch Notes", Material.BOOK, 1,
                        "§7View the latest features and",
                        "§7changes to the game.",
                        "",
                        "§eClick to open!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIPatchNotes());
                    return true;
                })
                .build());

        // Deliveries button
        attachItem(GUIItem.builder(13)
                .item(ItemStackCreator.getStack("§aDeliveries", Material.ENDER_CHEST, 1,
                        "§7Any items that may be delivered to",
                        "§7yourself or your island will appear",
                        "§7here for collection!",
                        "",
                        "§eClick to open!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cNo new deliveries.");
                    return true;
                })
                .build());

        // Visits button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§aVisits and Guestings", Material.EMERALD, 1,
                        "§7Learn all about how to §a/visit",
                        "§7players across the SkyBlock universe!",
                        " ",
                        "§eClick to learn!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIGuests());
                    return true;
                })
                .build());

        // Move Jerry button
        attachItem(GUIItem.builder(35)
                .item(ItemStackCreator.createNamedItemStack(Material.BEDROCK, "§aMove Jerry").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    ctx.player().sendMessage("§aI have given you an egg, place this where you would like me to move to!");
                    ctx.player().addAndUpdateItem(ItemType.MOVE_JERRY);
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}