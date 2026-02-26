package net.swofty.type.skyblockgeneric.gui.inventories.museum;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.i18n.I18n;

public class GUIMuseumAppraisal extends HypixelInventoryGUI {
    public GUIMuseumAppraisal() {
        super(I18n.string("gui_museum.appraisal.title"), InventoryType.CHEST_5_ROW);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
