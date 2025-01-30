package net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIProfileSelectMode extends SkyBlockAbstractInventory {

    public GUIProfileSelectMode() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Choose a SkyBlock Mode")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Profile Management").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileManagement());
                    return true;
                })
                .build());

        // Classic profile button
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§aClassic Profile", Material.GRASS_BLOCK, 1,
                        "§8SkyBlock Mode",
                        "",
                        "§7A SkyBlock adventure with the",
                        "§7default rules.",
                        "",
                        "§7Start on a new tiny island,",
                        "§7without gear and build your",
                        "§7way up to become the",
                        "§agreatest player§7 in the",
                        "§7universe!",
                        "",
                        "§eClick to play this mode!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileCreate());
                    return true;
                })
                .build());

        // Special modes button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§6Special Modes", Material.BLAZE_POWDER, 1,
                        "§7Choose a SkyBlock mode with",
                        "§7special rules and unique",
                        "§7mechanics.",
                        "",
                        "§eClick to choose a mode!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage("§cSpecial Modes in SkyBlock are currently unavailable. Please check back another time.");
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}