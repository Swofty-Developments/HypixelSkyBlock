package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class FireballShopItem extends ShopItem {

	public FireballShopItem() {
		super("Fireball", "A powerful explosive that can be thrown to cause destruction.", 40, 1, Currency.IRON, Material.FIRE_CHARGE);
	}

	@Override
	public void onPurchase(Player player) {
		//player.getInventory().addItemStack(Server.getInstance().getItemHandler().getItemById("fireball").getItemStack());
	}

}
