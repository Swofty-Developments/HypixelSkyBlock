package net.swofty.type.skywarslobby.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skywarslobby.SkywarsLobbyScoreboard;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		SkywarsLobbyScoreboard.removeCache(player);
	}
}

