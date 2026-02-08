package net.swofty.type.replayviewer.item.impl;

import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.replayviewer.item.ReplayItem;

public class TeleporterItem extends ReplayItem {

	public TeleporterItem() {
		super("teleporter");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.COMPASS, "§aTeleport to Player").build();
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {

	}
}
