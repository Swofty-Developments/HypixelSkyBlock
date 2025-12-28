package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class Wool extends ShopItem {

	public Wool() {
		super("wool", "Wool", "Great for bridging across islands.\nTurns into your team's color.", 4, 16, Currency.IRON, Material.WHITE_WOOL);
	}

	private Material mapTeamToWool(BedWarsMapsConfig.TeamKey teamKey) {
		return switch (teamKey) {
			case RED -> Material.RED_WOOL;
			case BLUE -> Material.BLUE_WOOL;
			case GREEN -> Material.LIME_WOOL;
			case YELLOW -> Material.YELLOW_WOOL;
			case AQUA -> Material.LIGHT_BLUE_WOOL;
			case PINK -> Material.PINK_WOOL;
			case WHITE -> Material.WHITE_WOOL;
			case GRAY -> Material.GRAY_WOOL;
		};
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		Material woolMaterial = mapTeamToWool(player.getTeamKey());
		player.getInventory().addItemStack(ItemStack.builder(woolMaterial)
				.amount(16)
				.build());
	}

}
