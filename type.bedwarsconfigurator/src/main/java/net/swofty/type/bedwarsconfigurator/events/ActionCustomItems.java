package net.swofty.type.bedwarsconfigurator.events;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.bedwarsconfigurator.TypeBedWarsConfiguratorLoader;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionCustomItems implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemOnBlockEvent event) {
		TypeBedWarsConfiguratorLoader.getItemHandler().onItemUseOnBlock(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerFinishItemUseEvent event) {
		TypeBedWarsConfiguratorLoader.getItemHandler().onItemFinishUse(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(PlayerUseItemEvent event) {
		TypeBedWarsConfiguratorLoader.getItemHandler().onItemUse(event);
	}

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = true)
	public void run(ItemDropEvent event) {
		TypeBedWarsConfiguratorLoader.getItemHandler().onItemDrop(event);
	}

}
