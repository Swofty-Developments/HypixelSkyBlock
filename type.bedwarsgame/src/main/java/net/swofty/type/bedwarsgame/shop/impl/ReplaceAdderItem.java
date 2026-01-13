package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.function.Function;

public class ReplaceAdderItem extends ShopItem {

	private final Material replacementMaterial;

	public ReplaceAdderItem(String id, String name, String description, Function<BedwarsGameType, Integer> cost, Currency currency, Material material) {
		super(id, name, description, cost, 1, currency, material);
		this.replacementMaterial = Material.WOODEN_SWORD;
	}

	public ReplaceAdderItem(String id, String name, String description, int cost, Currency currency, Material material) {
		super(id, name, description, cost, 1, currency, material);
		this.replacementMaterial = Material.WOODEN_SWORD;
	}

	public ReplaceAdderItem(String id, String name, String description, int cost, Currency currency, Material material, Material replacementMaterial) {
		super(id, name, description, cost, 1, currency, material);
		this.replacementMaterial = replacementMaterial;
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		var inventory = player.getInventory();
		for (int i = 0; i < inventory.getSize(); i++) {
			var item = inventory.getItemStack(i);
			if (item.material() == replacementMaterial) {
				inventory.setItemStack(i, item.withMaterial(this.getDisplay().material()));
				return;
			}
		}

		inventory.addItemStack(getDisplay());
		if (player.hasTag(Tag.Integer("upgrade_sharpness"))) {
			Game game = player.getGame();
			TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("sharpness").applyEffect(game, player.getTeamKey(), player.getTag(Tag.Integer("upgrade_sharpness")));
		}
	}

}
