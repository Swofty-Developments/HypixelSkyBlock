package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.starter;

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

public class GUIStarterSkills extends SkyBlockAbstractInventory {
    public GUIStarterSkills() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Starter -> Skills")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        // Fill background
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Setup close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
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
        // No special cleanup needed
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed for sudden quit
    }
}