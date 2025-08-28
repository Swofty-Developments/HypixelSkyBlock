package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class Collectibles extends BedWarsItem {

	public Collectibles() {
		super("collectibles");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.CHEST, "§aCollectibles §7(Right Click)").build();
	}

}
