package net.swofty.type.bedwarsgame.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.commons.game.GameState;
import net.swofty.type.bedwarsgame.BedWarsGameScoreboard;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@SneakyThrows
	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		final BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		BedWarsGame game = player.getGame();
		if (game != null) {
			if (game.getGameStatus() == GameState.IN_PROGRESS) {
				game.handleDisconnect(player);
			} else {
				game.leave(player);
			}
		}
		BedWarsGameScoreboard.removeCache(player);
	}
}

