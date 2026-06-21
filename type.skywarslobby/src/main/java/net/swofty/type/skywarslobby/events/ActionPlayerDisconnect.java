package net.swofty.type.skywarslobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.SkyWarsLobbyScoreboard;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
	public void run(PlayerDisconnectEvent event) {
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		SkyWarsLobbyScoreboard.removeCache(player);
	}
}

