package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class BridgeEggShopItem extends ShopItem {

	public BridgeEggShopItem() {
		super("bridge_egg", "Bridge Egg", "This egg creates a bridge in its trail\nafter being thrown.", 1, 1, Currency.EMERALD, Material.EGG);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		player.getInventory().addItemStack(TypeBedWarsGameLoader.getItemHandler().getItem("bridge_egg").getItemStack());
	}

}
