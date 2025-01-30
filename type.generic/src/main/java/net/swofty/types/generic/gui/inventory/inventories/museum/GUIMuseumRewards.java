package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIMuseumRewards extends SkyBlockAbstractInventory {
    public GUIMuseumRewards() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Museum Rewards")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Setup GUI items when implemented
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }
}