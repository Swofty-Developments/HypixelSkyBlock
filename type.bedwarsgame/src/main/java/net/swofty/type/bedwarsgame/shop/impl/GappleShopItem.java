package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class GappleShopItem extends ShopItem {

	public GappleShopItem() {
		super("Golden Apple", "Well-rounded healing.", 3, 1, Currency.GOLD, Material.GOLDEN_APPLE);
	}

	@Override
	public void onPurchase(Player player) {
		player.getInventory().addItemStack(TypeBedWarsGameLoader.getItemHandler().getItem("golden_apple").getItemStack());
	}

}
