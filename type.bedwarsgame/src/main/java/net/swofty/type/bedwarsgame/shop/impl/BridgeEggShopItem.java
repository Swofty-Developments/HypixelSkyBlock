package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class BridgeEggShopItem extends ShopItem {

	public BridgeEggShopItem() {
		super("Bridge Egg", "A powerful bridging thing.", 1, 1, Currency.EMERALD, Material.EGG);
	}

	@Override
	public void onBought(Player player) {
		//player.getInventory().addItemStack(TypeBedWarsGameLoader.getItemHandler().getItemById("bridge_egg").getItemStack());
	}

}
