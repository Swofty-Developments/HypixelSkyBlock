package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class PopupTowerItem extends ShopItem {

	public PopupTowerItem() {
		super("Pop-Up Tower", "Place a pop-up defence!", 24, 1, Currency.IRON, Material.CHEST);
	}

	@Override
	public void onPurchase(Player player) {
		player.getInventory().addItemStack(TypeBedWarsGameLoader.getItemHandler().getItem("popup_tower").getItemStack());
	}

}
