package net.swofty.type.generic.event.actions.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryClose implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
	public void run(InventoryCloseEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
			HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

			if (gui == null) return;

			gui.onClose(event, HypixelInventoryGUI.CloseReason.PLAYER_EXITED);
			HypixelInventoryGUI.GUI_MAP.remove(player.getUuid());
		}
	}
}
