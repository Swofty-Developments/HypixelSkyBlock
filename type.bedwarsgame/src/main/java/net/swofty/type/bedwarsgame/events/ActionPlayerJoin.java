package net.swofty.type.bedwarsgame.events;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.swofty.commons.ServerType;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.redis.service.RedisGameMessage;
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

		MathUtility.delay(() -> tryJoinGame(player, false), 15);
	}

	private void tryJoinGame(BedWarsPlayer player, boolean isRetry) {
		if (!player.isOnline()) return;

		String preferredGameId = RedisGameMessage.game.remove(player.getUuid());
		if (preferredGameId == null) {
			if (!isRetry) {
				Logger.info("No game assignment found for " + player.getUsername() + ", retrying in 1 second...");
				MathUtility.delay(() -> tryJoinGame(player, true), 20);
				return;
			}
			Logger.error("Failed to find game assignment for player " + player.getUsername());
			player.sendMessage("§cNo game assignment found! Returning to lobby...");
			player.sendTo(ServerType.BEDWARS_LOBBY);
			return;
		}

		Game preferred = TypeBedWarsGameLoader.getGameById(preferredGameId);
		if (preferred == null) {
			player.sendMessage("§cThe assigned game no longer exists! Returning to lobby...");
			player.sendTo(ServerType.BEDWARS_LOBBY);
			return;
		}

		MathUtility.delay(() -> {
			if (!player.isOnline()) return;
			if (preferred.hasDisconnectedPlayer(player.getUuid())) {
				preferred.rejoin(player);
			} else {
				preferred.join(player);
			}
		}, 15);
	}
}

