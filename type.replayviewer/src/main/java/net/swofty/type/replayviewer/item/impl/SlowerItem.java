package net.swofty.type.replayviewer.item.impl;

import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.item.ReplayItem;
import net.swofty.type.replayviewer.playback.ReplaySession;

public class SlowerItem extends ReplayItem {

	public SlowerItem() {
		super("slower");
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.getStackHead("§aDecrease Speed", "dcd7c14b92cb37909208a0d204780493f9c9cc5f56d1019b7363417909f1d956").build();
	}

	@Override
	public void onItemInteract(PlayerInstanceEvent event) {
		((CancellableEvent) event).setCancelled(true);
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
			ReplaySession::cycleSpeedDown,
			() -> player.sendMessage("§cNo active replay session.")
		);
	}
}
