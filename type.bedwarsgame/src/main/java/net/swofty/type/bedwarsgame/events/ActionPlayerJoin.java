package net.swofty.type.bedwarsgame.events;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {

	@SneakyThrows
	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(AsyncPlayerConfigurationEvent event) {
		final BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		Logger.info("Player " + player.getUsername() + " joined the server from origin server " + player.getOriginServer());
		event.setSpawningInstance(HypixelConst.getEmptyInstance());
		player.setRespawnPoint(new Pos(10, 10, 10));

		if (!TypeBedWarsGameLoader.getGames().isEmpty()) {
			TypeBedWarsGameLoader.games.getFirst().join(player);
		} else {
			Logger.info("No games found, creating a new game for player " + player.getUsername()); //dev
			Game game = TypeBedWarsGameLoader.createGame(TypeBedWarsGameLoader.getMapsConfig().getMaps().getFirst());
			if (game == null) {
				Logger.error("Failed to create a new game for player " + player.getUsername());
				return;
			}
			MathUtility.delay(
					() -> game.join(player),
					15
			);
		}
	}
}

