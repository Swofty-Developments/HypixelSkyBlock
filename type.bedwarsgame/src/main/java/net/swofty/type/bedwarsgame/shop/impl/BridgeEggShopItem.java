package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class BridgeEggShopItem extends ShopItem {

	public BridgeEggShopItem() {
		super("Bridge Egg", "This egg creates a bridge in its trail\nafter being thrown.", 1, 1, Currency.EMERALD, Material.EGG);
	}

	@Override
	public void onPurchase(Player player) {
		player.getInventory().addItemStack(TypeBedWarsGameLoader.getItemHandler().getItem("bridge_egg").getItemStack());
	}

}
