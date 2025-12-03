package net.swofty.service.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.BedwarsGameType;
import net.swofty.commons.game.Game;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OrchestratorCache {
	private static final Map<String, GameServerState> serversByShortName = new ConcurrentHashMap<>();
	private static final Map<UUID, GameWithServer> gamesByGameId = new ConcurrentHashMap<>();
	private static final long HEARTBEAT_TTL_MS = 20000; // 20s

	public static void handleHeartbeat(UUID uuid,
									   String shortName,
									   ServerType type,
									   int maxPlayers,
									   int onlinePlayers,
									   List<Game> games) {
		GameServerState state = new GameServerState(
				uuid,
				shortName,
				type,
				maxPlayers,
				onlinePlayers,
				Instant.now().toEpochMilli()
		);
		serversByShortName.put(shortName, state);

		// Remove old games from this server
		gamesByGameId.entrySet().removeIf(entry -> entry.getValue().serverUuid().equals(uuid));

		// Add current games with server tracking
		for (Game game : games) {
			gamesByGameId.put(game.getGameId(), new GameWithServer(game, uuid, shortName));
		}
	}

	/**
	 * Finds an existing joinable game for the specified game type and map
	 */
	public static GameWithServer findExisting(BedwarsGameType gameType, String map) {
		cleanup();

		List<GameWithServer> candidates = new ArrayList<>();
		for (GameWithServer gameWithServer : gamesByGameId.values()) {
			Game game = gameWithServer.game();
			if (game.getType() == ServerType.BEDWARS_GAME &&
					isGameJoinable(game, gameType) &&
					(map == null || game.getMap().equals(map))) {
				candidates.add(gameWithServer);
			}
		}

		if (candidates.isEmpty()) {
			return null;
		}

		// Prefer games with more players (closer to starting)
		candidates.sort(Comparator.comparingInt((GameWithServer g) -> g.game().getInvolvedPlayers().size()).reversed());
		return candidates.get(0);
	}

	/**
	 * Finds a server with capacity to host a new game
	 */
	public static GameServerState instantiateServer(BedwarsGameType gameType, String map) {
		cleanup();

		List<GameServerState> candidates = new ArrayList<>();
		for (GameServerState server : serversByShortName.values()) {
			if (server.type() == ServerType.BEDWARS_GAME &&
					server.canHostNewGame(gameType)) {
				candidates.add(server);
			}
		}

		if (candidates.isEmpty()) {
			return null;
		}

		// Prefer servers with more available capacity
		candidates.sort(Comparator.comparingInt(GameServerState::availableSlots).reversed());
		int topAvail = candidates.get(0).availableSlots();

		List<GameServerState> topServers = new ArrayList<>();
		for (GameServerState server : candidates) {
			if (server.availableSlots() == topAvail) {
				topServers.add(server);
			} else {
				break;
			}
		}

		return topServers.get(ThreadLocalRandom.current().nextInt(topServers.size()));
	}

	public static Set<String> getMaps(ServerType type, String gameTypeName) {
		cleanup();
		Set<String> maps = new HashSet<>();

		for (GameWithServer gameWithServer : gamesByGameId.values()) {
			Game game = gameWithServer.game();
			if (game.getType() == type) {
				maps.add(game.getMap());
			}
		}

		return maps;
	}

	private static boolean isGameJoinable(Game game, BedwarsGameType gameType) {
		int maxPlayersForType = gameType.maxPlayers();
		int currentPlayers = game.getInvolvedPlayers().size();
		return currentPlayers < maxPlayersForType;
	}

	private static void cleanup() {
		long now = Instant.now().toEpochMilli();

		// Remove stale servers
		serversByShortName.values().removeIf(server ->
				now - server.lastHeartbeat() > HEARTBEAT_TTL_MS);

		// Remove games from servers that are no longer available
		Set<UUID> availableServerUuids = new HashSet<>();
		for (GameServerState server : serversByShortName.values()) {
			availableServerUuids.add(server.uuid());
		}

		gamesByGameId.values().removeIf(gameWithServer ->
				!availableServerUuids.contains(gameWithServer.serverUuid()));
	}

	public record GameServerState(UUID uuid,
								  String shortName,
								  ServerType type,
								  int maxPlayers,
								  int onlinePlayers,
								  long lastHeartbeat) {

		public int availableSlots() {
			return Math.max(0, maxPlayers - onlinePlayers);
		}

		public boolean canHostNewGame(BedwarsGameType gameType) {
			return availableSlots() >= gameType.maxPlayers();
		}
	}

	public record GameWithServer(Game game, UUID serverUuid, String serverShortName) {
	}

	// Getter methods for debugging/monitoring
	public static Collection<GameServerState> getAllServers() {
		cleanup();
		return new ArrayList<>(serversByShortName.values());
	}

	public static Collection<GameWithServer> getAllActiveGames() {
		cleanup();
		return new ArrayList<>(gamesByGameId.values());
	}

	public static GameServerState getServerByUuid(UUID serverUuid) {
		return serversByShortName.values().stream()
				.filter(server -> server.uuid().equals(serverUuid))
				.findFirst()
				.orElse(null);
	}
}