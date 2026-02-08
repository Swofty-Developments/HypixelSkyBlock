package net.swofty.type.replayviewer.event;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;

public class PlayerLeaveEvent implements HypixelEventClass {
	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		TypeReplayViewerLoader.removeSession(event.getPlayer().getUuid());
	}
}
