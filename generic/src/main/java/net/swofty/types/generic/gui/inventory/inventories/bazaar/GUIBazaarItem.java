package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIBazaarItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    public GUIBazaarItem(ItemType itemType) {
        super("few", InventoryType.CHEST_4_ROW);
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
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
