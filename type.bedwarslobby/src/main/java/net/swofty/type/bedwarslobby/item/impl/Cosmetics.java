package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class Cosmetics extends BedWarsItem {

	public Cosmetics() {
		super("cosmetics");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.CHEST, "§aCosmetics").build();
	}

}
