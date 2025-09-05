package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;

@Getter
public abstract class ShopItem {

	private final String name;
	private final String description;
	private final int price;
	private final int amount;
	private final Currency currency;
	private final ItemStack display;

	public ShopItem(String name, String description, int price, int amount, Currency currency, Material display) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.amount = amount;
		this.currency = currency;
		this.display = ItemStack.of(display);
	}

	/**
	 * Called when a player purchases an item. This method should be used to give items to the player or apply effects.
	 *
	 * @param player the player who purchasd the item
	 */
	public abstract void onPurchase(Player player);

	public boolean isAvailable(Player player) {
		return true;
	}

	/**
	 * Handles the purchase of an item meanwhile taking the currency from the player's inventory.
	 *
	 * @param player the player making the purchase
	 */
	public void handlePurchase(Player player) {
		BedWarsInventoryManipulator.removeItems(player, currency.getMaterial(), price);
		onPurchase(player);
	}

}
