package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class GappleShopItem extends ShopItem {

	public GappleShopItem() {
		super("Golden Apple", "A powerful eating thing.", 3, 1, Currency.GOLD, Material.GOLDEN_APPLE);
	}

	@Override
	public void onPurchase(Player player) {
		//player.getInventory().addItemStack(Server.getInstance().getItemHandler().getItemById("goldenapple").getItemStack());
	}

}
