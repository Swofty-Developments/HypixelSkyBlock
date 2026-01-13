package net.swofty.type.lobby.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class LobbyPlayerMove implements HypixelEventClass {
	private final Pos spawnPoint;

	public LobbyPlayerMove(Pos spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getPosition().y() < 0) {
			player.teleport(spawnPoint);
			player.sendMessage("§cYou are not allowed to leave this area!");
		}
	}
}
