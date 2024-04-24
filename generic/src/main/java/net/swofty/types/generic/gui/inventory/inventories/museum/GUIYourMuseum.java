package net.swofty.types.generic.gui.inventory.inventories.museum;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;

public class GUIYourMuseum extends SkyBlockInventoryGUI {
    public GUIYourMuseum() {
        super("Your Museum", InventoryType.CHEST_6_ROW);
    }
    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
