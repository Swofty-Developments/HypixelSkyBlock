package net.swofty.type.bedwarsgame.events;

import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.user.ExperienceCause;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionGamePickup implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
	public void run(PickupItemEvent event) {
		ItemStack itemStack = event.getItemEntity().getItemStack();
		if (event.getLivingEntity() instanceof BedWarsPlayer player) {
			player.getInventory().addItemStack(itemStack);

			// handle bedwars xp for diamonds and emeralds
			for (Currency currency : Currency.values()) {
				if (itemStack.material().equals(currency.getMaterial())) {
					switch (currency) {
						case DIAMOND:
							player.xp(ExperienceCause.DIAMONDS);
						case EMERALD:
							player.xp(ExperienceCause.EMERALDS);
					}
					break;
				}
			}
		}
	}

}
