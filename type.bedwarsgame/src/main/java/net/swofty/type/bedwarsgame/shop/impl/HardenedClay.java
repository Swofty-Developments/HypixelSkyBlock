package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgeneric.game.BedWarsMapsConfig;

public class HardenedClay extends ShopItem {

	public HardenedClay() {
		super("Hardened Clay", "Basic block to defend your bed.", 12, 16, Currency.IRON, Material.TERRACOTTA);
	}

	private Material mapTeamToTerracotta(BedWarsMapsConfig.TeamKey teamKey) {
		return switch (teamKey) {
			case RED -> Material.RED_TERRACOTTA;
			case BLUE -> Material.BLUE_TERRACOTTA;
			case GREEN -> Material.GREEN_TERRACOTTA;
			case YELLOW -> Material.YELLOW_TERRACOTTA;
			case AQUA -> Material.LIGHT_BLUE_TERRACOTTA;
			case PINK -> Material.PINK_TERRACOTTA;
			case WHITE -> Material.WHITE_TERRACOTTA;
			case GRAY -> Material.GRAY_TERRACOTTA;
		};
	}

	@Override
	public void onPurchase(Player player) {
		BedWarsPlayer bwPlayer = (BedWarsPlayer) player;
		Material terracottaMaterial = mapTeamToTerracotta(bwPlayer.getTeamKey());
		player.getInventory().addItemStack(ItemStack.builder(terracottaMaterial)
				.amount(16)
				.build());
	}

}
