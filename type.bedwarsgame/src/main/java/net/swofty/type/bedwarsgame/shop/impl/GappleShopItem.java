package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class GappleShopItem extends ShopItem {

	public GappleShopItem() {
		super("golden_apple", "Golden Apple", "Well-rounded healing.", 3, 1, Currency.GOLD, Material.GOLDEN_APPLE);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		player.getInventory().addItemStack(TypeBedWarsGameLoader.getItemHandler().getItem("golden_apple").getItemStack());
	}

}
