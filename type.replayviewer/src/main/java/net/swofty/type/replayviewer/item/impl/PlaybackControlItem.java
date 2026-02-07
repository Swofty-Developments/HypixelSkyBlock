package net.swofty.type.replayviewer.item.impl;

import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.item.ReplayItem;

import java.util.List;

public class PlaybackControlItem extends ReplayItem {

	public PlaybackControlItem() {
		super("playback");
	}

	@Override
	public ItemStack getBlandItem() {
		return null;
	}

	@Override
	public ItemStack getItemStack(HypixelPlayer... p) {
		HypixelPlayer player = p[0];
		return ItemStackCreator.getStack("§aClick to Resume", Material.GRAY_DYE, 1, List.of(
			"§7The replay is currently paused."
		)).build();
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);

	}
}
