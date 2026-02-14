package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.potion.PotionType;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;

public class PotionShopItem extends ShopItem {

	final PotionType potionType;

	public PotionShopItem(String id, String name, String description, int cost, int amount, Currency currency, PotionType potionType) {
		super(id, name, description, cost, amount, currency, Material.POTION, DatapointBedWarsHotbar.HotbarItemType.POTIONS);
		this.potionType = potionType;
	}

	@Override
	public ItemStack getDisplay() {
		return ItemStack.builder(Material.POTION)
				.set(DataComponents.POTION_CONTENTS, new PotionContents(potionType))
				.build();
	}

	@Override
	public void onPurchase(BedWarsPlayer player) {
		PotionContents potionContents = new PotionContents(potionType);
		giveItem(player, ItemStack.builder(Material.POTION).set(DataComponents.POTION_CONTENTS, potionContents).build());
	}
}
