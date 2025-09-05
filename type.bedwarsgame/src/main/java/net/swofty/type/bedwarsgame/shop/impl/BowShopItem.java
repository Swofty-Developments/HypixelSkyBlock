package net.swofty.type.bedwarsgame.shop.impl;

import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.bedwarsgame.shop.ShopItem;

public class BowShopItem extends ShopItem {

	final EnchantmentList enchantmentList;

	public BowShopItem(String name, String description, int price, Currency currency, EnchantmentList enchantmentList) {
		super(name, description, price, 1, currency, Material.BOW);
		this.enchantmentList = enchantmentList;
	}

	@Override
	public ItemStack getDisplay() {
		return super.getDisplay().builder().set(DataComponents.ENCHANTMENTS, enchantmentList).build();
	}

	@Override
	public void onPurchase(Player player) {
		ItemStack bow = ItemStack.of(Material.BOW).builder().set(DataComponents.ENCHANTMENTS, enchantmentList).build();
		player.getInventory().addItemStack(bow);
	}
}
