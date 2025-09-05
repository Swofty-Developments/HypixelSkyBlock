package net.swofty.type.bedwarsgame.events;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.redis.RedisBedWarsJoinPreference;
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

		// honor lobby preference first. Also currently has a double delay, look into it.
		MathUtility.delay(
				() -> {
					Game preferred = null;
					var pref = RedisBedWarsJoinPreference.preferences.remove(player.getUuid());
					if (pref != null) {
						String wantedMap = pref.map();
						if (wantedMap != null && !wantedMap.isBlank()) {
							for (Game g : TypeBedWarsGameLoader.getGames()) {
								if (g.getMapEntry() != null && wantedMap.equalsIgnoreCase(g.getMapEntry().getId())) {
									preferred = g;
									break;
								}
							}
						}
						// if only mode provided or map not found, pick a random available game
						if (preferred == null && !TypeBedWarsGameLoader.getGames().isEmpty()) {
							java.util.List<Game> all = TypeBedWarsGameLoader.getGames();
							preferred = all.get((int) (Math.random() * all.size()));
						}
					}

					if (preferred != null) {
						Game finalPreferred = preferred;
						MathUtility.delay(
								() -> finalPreferred.join(player),
								15
						);
					} else if (!TypeBedWarsGameLoader.getGames().isEmpty()) {
						TypeBedWarsGameLoader.getGames().getFirst().join(player);
					} else {
						Logger.info("No games found, creating a new game for player " + player.getUsername()); //dev
						Game game;
						if (pref != null && pref.map() != null && !pref.map().isBlank()) {
							var maps = TypeBedWarsGameLoader.getMapsConfig().getMaps();
							var wanted = maps.stream().filter(m -> pref.map().equalsIgnoreCase(m.getId())).findFirst().orElse(null);
							game = wanted != null ? TypeBedWarsGameLoader.createGame(wanted) :
									TypeBedWarsGameLoader.createGame(TypeBedWarsGameLoader.getMapsConfig().getMaps().getFirst());
						} else {
							game = TypeBedWarsGameLoader.createGame(TypeBedWarsGameLoader.getMapsConfig().getMaps().getFirst());
						}
						if (game == null) {
							Logger.error("Failed to create a new game for player " + player.getUsername());
							return;
						}
						MathUtility.delay(
								() -> game.join(player),
								15
						);
					}
				}, 15);
	}
}

