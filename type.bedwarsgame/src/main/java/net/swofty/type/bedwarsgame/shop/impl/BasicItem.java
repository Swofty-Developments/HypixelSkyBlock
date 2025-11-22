package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class BasicItem extends ShopItem {

	public BasicItem(String name, String description, int cost, int amount, Currency currency, Material material) {
		super(name, description, cost, amount, currency, material);
	}

	@Override
	public void onPurchase(Player player) {
		player.getInventory().addItemStack(this.getDisplay().withAmount(this.getAmount()));
	}

}
