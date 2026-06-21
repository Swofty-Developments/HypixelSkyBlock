package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;

public class Wool extends ShopItem {

	public Wool() {
		super("wool", "Wool", "Great for bridging across islands.\nTurns into your team's color.", 4, 16, Currency.IRON, Material.WHITE_WOOL,
			DatapointBedWarsHotbar.HotbarItemType.BLOCKS);
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		giveItem(player, ItemStack.builder(player.getTeamKey().woolMaterial())
				.amount(16)
				.build());
	}

}
