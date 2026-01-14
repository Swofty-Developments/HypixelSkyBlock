package net.swofty.type.murdermysterygame.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.murdermysterygame.MurderMysteryGameScoreboard;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerDisconnect implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerDisconnectEvent event) {
		MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();

		Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
		if (game != null) {
			game.disconnect(player);
		}

		MurderMysteryGameScoreboard.removeCache(player);
	}
}
