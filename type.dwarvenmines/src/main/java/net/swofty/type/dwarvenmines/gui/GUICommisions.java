package net.swofty.type.dwarvenmines.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;

public class GUICommisions extends HypixelInventoryGUI {

	public GUICommisions() {
		super("Commissions", InventoryType.CHEST_4_ROW);
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(FILLER_ITEM);

		set(GUIClickableItem.getCloseItem(31));
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}
}
