package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarItemSet;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

public class GUIBazaarItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    public GUIBazaarItem(BazaarCategories category, BazaarItemSet itemSet) {
        super(StringUtility.toNormalCase(category.name()) + " -> " + itemSet.displayName, InventoryType.CHEST_4_ROW);
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {

    }

    @Override
    public int refreshRate() {
        return 0;
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
