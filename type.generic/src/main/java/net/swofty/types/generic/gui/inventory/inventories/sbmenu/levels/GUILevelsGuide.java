package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.levels.CustomLevelAward;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUILevelsGuide extends SkyBlockInventoryGUI {
    public GUILevelsGuide() {
        super("Guide", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        SkyBlockPlayer player = e.player();
        if (player.hasCustomLevelAward(CustomLevelAward.ACCESS_TO_WARDROBE)) {

        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
