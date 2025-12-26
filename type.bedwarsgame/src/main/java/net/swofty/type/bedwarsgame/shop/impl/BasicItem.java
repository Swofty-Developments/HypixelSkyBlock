package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class BasicItem extends ShopItem {

	public BasicItem(String id, String name, String description, int cost, int amount, Currency currency, Material material) {
		super(id, name, description, cost, amount, currency, material);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		player.getInventory().addItemStack(this.getDisplay().withAmount(this.getAmount()));
	}

}
