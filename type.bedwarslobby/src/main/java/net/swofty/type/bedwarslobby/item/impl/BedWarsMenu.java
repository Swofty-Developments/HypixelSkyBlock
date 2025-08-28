package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class BedWarsMenu extends BedWarsItem {

	public BedWarsMenu() {
		super("bedwars_menu");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.EMERALD, "§aBed Wars Menu & Shop §7(Right Click)").build();
	}

}
