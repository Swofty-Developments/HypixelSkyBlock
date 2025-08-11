package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;

public class GUIStarterSkills extends HypixelInventoryGUI {
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
