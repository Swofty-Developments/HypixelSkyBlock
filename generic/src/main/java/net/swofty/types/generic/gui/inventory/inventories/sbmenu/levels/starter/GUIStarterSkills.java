package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.starter;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;

public class GUIStarterSkills extends SkyBlockInventoryGUI {
    public GUIStarterSkills() {
        super("Starter -> Skills", InventoryType.CHEST_6_ROW);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
