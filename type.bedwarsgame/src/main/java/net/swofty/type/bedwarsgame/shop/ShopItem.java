package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.util.InventoryManipulation;

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

	public abstract void onBought(Player player);

	public boolean canBeBought(Player player) {
		return true;
	}

	/**
	 * Handles the purchase of this item by the player.
	 *
	 * @param player the player making the purchase
	 */
	public void handlePurchase(Player player) {
		InventoryManipulation.removeItems(player, currency.getMaterial(), price);
		onBought(player);
	}

}
