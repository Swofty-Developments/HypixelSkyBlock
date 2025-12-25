package net.swofty.type.generic.event.actions.gui;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryPostClick implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(InventoryClickEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
			HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

			if (gui == null) return;

			int slot = event.getSlot();
			GUIItem item = gui.get(slot);

			if (item == null) return;

			if (item instanceof GUIClickableItem clickable) {
				clickable.runPost(event, player);
			}
		}
	}
}
