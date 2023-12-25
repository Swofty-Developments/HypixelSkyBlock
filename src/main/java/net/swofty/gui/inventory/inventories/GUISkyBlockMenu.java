package net.swofty.gui.inventory.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.item.items.miscellaneous.SkyBlockMenu;
import net.swofty.user.SkyBlockPlayer;

public class GUISkyBlockMenu extends SkyBlockInventoryGUI {
    public GUISkyBlockMenu() {
        super("SkyBlock Menu", InventoryType.CHEST_6_ROW);
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

    }
}
