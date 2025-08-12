package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.levels.CustomLevelAward;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUILevelsGuide extends HypixelInventoryGUI {
    public GUILevelsGuide() {
        super("Guide", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
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
