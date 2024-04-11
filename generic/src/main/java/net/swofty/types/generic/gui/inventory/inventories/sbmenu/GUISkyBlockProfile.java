package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;

public class GUISkyBlockProfile extends SkyBlockInventoryGUI {

    public GUISkyBlockProfile() {
        super("Your Equipment and Stats", InventoryType.CHEST_6_ROW);
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(48));
        set(GUIClickableItem.getGoBackItem(49, new GUISkyBlockMenu()));
    }

    @Override
    public void onOpen(final InventoryGUIOpenEvent e) {
    }


    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
