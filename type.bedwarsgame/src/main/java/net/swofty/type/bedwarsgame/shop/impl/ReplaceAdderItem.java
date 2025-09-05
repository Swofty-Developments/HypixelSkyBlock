package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class ReplaceAdderItem extends ShopItem {

	private final Material replacementMaterial;

	public ReplaceAdderItem(String name, String description, int cost, Currency currency, Material material) {
		super(name, description, cost, 1, currency, material);
		this.replacementMaterial = Material.WOODEN_SWORD;
	}

	public ReplaceAdderItem(String name, String description, int cost, Currency currency, Material material, Material replacementMaterial) {
		super(name, description, cost, 1, currency, material);
		this.replacementMaterial = replacementMaterial;
	}

	@Override
	public void onPurchase(net.minestom.server.entity.Player player) {
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
			String team = player.getTag(Tag.String("team"));
			String gameId = player.getTag(Tag.String("gameId"));

			TypeBedWarsGameLoader.getTeamShopManager().getUpgrade("sharpness").applyEffect(TypeBedWarsGameLoader.getGameById(gameId), team, player.getTag(Tag.Integer("upgrade_sharpness")));
		}
	}

}
