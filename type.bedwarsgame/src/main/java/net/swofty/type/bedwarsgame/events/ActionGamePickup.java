package net.swofty.type.bedwarsgame.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionGamePickup implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
	public void run(PickupItemEvent event) {
		ItemStack itemStack = event.getItemEntity().getItemStack();
		if (event.getLivingEntity() instanceof Player player) {
			player.getInventory().addItemStack(itemStack);
		}
	}

}
