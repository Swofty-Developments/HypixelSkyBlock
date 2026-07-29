package net.swofty.type.replayviewer.event;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;

public class PlayerLeaveEvent implements HypixelEventClass {
	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		TypeReplayViewerLoader.getSession(event.getPlayer().getUuid()).ifPresent(session -> {
			session.removeViewer(event.getPlayer());
		});
	}
}
