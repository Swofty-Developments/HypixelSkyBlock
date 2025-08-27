package net.swofty.type.bedwarslobby.events;

import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.bedwarslobby.TypeBedWarsLobbyLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionCustomItems implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemOnBlockEvent event) {
		TypeBedWarsLobbyLoader.getItemHandler().onItemUseOnBlock(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerFinishItemUseEvent event) {
		TypeBedWarsLobbyLoader.getItemHandler().onItemFinishUse(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemEvent event) {
		TypeBedWarsLobbyLoader.getItemHandler().onItemUse(event);
	}

}
