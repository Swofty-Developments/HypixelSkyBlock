package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;

public class PopupTowerItem extends ShopItem {

	public PopupTowerItem() {
		super("pop_up_tower", "Pop-Up Tower", "Place a pop-up defence!", 24, 1, Currency.IRON, Material.CHEST,
			DatapointBedWarsHotbar.HotbarItemType.UTILITY);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		giveItem(player, TypeBedWarsGameLoader.getItemHandler().getItem("popup_tower").getItemStack());
	}

}
