package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;

public class GUISkyBlockLevel extends SkyBlockInventoryGUI {
    public GUISkyBlockLevel(SkyBlockLevelRequirement levelRequirement) {
        super("Level " + levelRequirement.asInt() + " Rewards", InventoryType.CHEST_4_ROW);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
