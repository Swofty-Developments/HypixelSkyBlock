package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

public class PlayCompass extends BedWarsItem {

	public PlayCompass() {
		super("play_compass");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.COMPASS, "§aGame Selector").build();
	}

}
