package net.swofty.type.bedwarsgame.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.bedwarsgame.BedWarsGameScoreboard;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@SneakyThrows
	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		final BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		Game game = player.getGame();
		if (game != null) {
			if (game.getGameStatus() == GameStatus.IN_PROGRESS) {
				game.handleDisconnect(player);
			} else {
				game.leave(player);
			}
		}
		BedWarsGameScoreboard.removeCache(player);
	}
}

