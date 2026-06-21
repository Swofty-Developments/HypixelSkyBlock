package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.shop.TeamUpgradeId;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public class ArmorShopItem extends ShopItem {

	private final Material boots;
	private final Material leggings;
	private final int armorLevel;

	public ArmorShopItem(String id, String name, String description, int price, Currency currency, Material boots, Material leggings, int armorLevel) {
		super(id, name, description, price, 1, currency, boots);
		this.boots = boots;
		this.leggings = leggings;
		this.armorLevel = armorLevel;
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		player.setEquipment(EquipmentSlot.BOOTS, ItemStack.of(boots));
		player.setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(leggings));
		player.setTag(TypeBedWarsGameLoader.ARMOR_LEVEL_TAG, armorLevel);

		if (player.hasTag(TeamUpgradeId.REINFORCED_ARMOR.levelTag())) {
			TypeBedWarsGameLoader.getTeamShopManager().getUpgrade(TeamUpgradeId.REINFORCED_ARMOR).applyEffect(
					player.getGame(),
					player.getTeamKey(),
				player.getTag(TeamUpgradeId.REINFORCED_ARMOR.levelTag())
			);
		}
	}

	@Override
	public boolean isOwned(Player player) {
		return player.getEquipment(EquipmentSlot.BOOTS).material() != boots && player.getEquipment(EquipmentSlot.LEGGINGS).material() != leggings;
	}
}
