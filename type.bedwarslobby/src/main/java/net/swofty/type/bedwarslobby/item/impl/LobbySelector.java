package net.swofty.type.bedwarslobby.item.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgeneric.item.BedWarsItem;

public class LobbySelector extends BedWarsItem {

	public LobbySelector() {
		super("lobby_selector");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStack.of(Material.NETHER_STAR);
	}

}
