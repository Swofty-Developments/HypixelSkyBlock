package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;

public class FireballShopItem extends ShopItem {

	public FireballShopItem() {
		super("fireball", "Fireball", "Right-click to launch! Great to knock\nback enemies walking on thin bridges.", 40, 1, Currency.IRON, Material.FIRE_CHARGE,
			DatapointBedWarsHotbar.HotbarItemType.UTILITY);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		giveItem(player, TypeBedWarsGameLoader.getItemHandler().getItem("fireball").getItemStack());
	}

}
