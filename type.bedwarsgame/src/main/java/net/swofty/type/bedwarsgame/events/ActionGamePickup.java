package net.swofty.type.bedwarsgame.events;

import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.user.ExperienceCause;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.jetbrains.annotations.Nullable;

public class ActionGamePickup implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
	public void run(PickupItemEvent event) {
		ItemStack itemStack = event.getItemEntity().getItemStack();
		if (event.getLivingEntity() instanceof BedWarsPlayer player) {
			// Only allow players on survival mode to pickup items
			if (player.getGameMode() != GameMode.SURVIVAL) {
				event.setCancelled(true);
				return;
			}

			player.getInventory().addItemStack(itemStack);

			// handle bedwars xp for diamonds and emeralds
			for (Currency currency : Currency.values()) {
				if (itemStack.material().equals(currency.getMaterial())) {
					@Nullable CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
					if (data != null && !data.nbt().getBoolean("generator")) continue;
					int amount = itemStack.amount();
					switch (currency) {
						case DIAMOND:
							player.xp(ExperienceCause.DIAMONDS, amount);
							break;
						case EMERALD:
							player.xp(ExperienceCause.EMERALDS, amount);
							break;
					}
					break;
				}
			}
		}
	}

}
