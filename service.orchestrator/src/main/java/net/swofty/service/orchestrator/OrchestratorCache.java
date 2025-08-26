package net.swofty.service.orchestrator;

import net.swofty.commons.ServerType;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OrchestratorCache {

	private static final Map<String, GameServerState> serversByShortName = new ConcurrentHashMap<>();
	private static final long HEARTBEAT_TTL_MS = 20000; // 20s

	public static void handleHeartbeat(UUID uuid,
									   String shortName,
									   ServerType type,
									   Collection<String> maps,
									   int maxPlayers,
									   int onlinePlayers) {
		GameServerState state = new GameServerState(
				uuid,
				shortName,
				type,
				new HashSet<>(maps),
				maxPlayers,
				onlinePlayers,
				Instant.now().toEpochMilli()
		);
		serversByShortName.put(shortName, state);
	}

	public static Set<String> getMaps(ServerType type) {
		cleanup();
		Set<String> maps = new HashSet<>();
		for (GameServerState s : serversByShortName.values()) {
			if (s.type == type) maps.addAll(s.maps);
		}
		return maps;
	}

	public static GameServerState pickServerForMap(ServerType type, String map, int neededSlots) {
		cleanup();
		List<GameServerState> candidates = new ArrayList<>();
		for (GameServerState s : serversByShortName.values()) {
			if (s.type == type && s.maps.contains(map)) {
				if (neededSlots <= 0 || s.availableSlots() >= neededSlots) {
					candidates.add(s);
				}
			}
		}
		if (candidates.isEmpty()) return null;

		// prefer the server with the most available slots.. tie-break randomly
		candidates.sort(Comparator.comparingInt(GameServerState::availableSlots).reversed());
		int topAvail = candidates.getFirst().availableSlots();
		List<GameServerState> top = new ArrayList<>();
		for (GameServerState c : candidates) {
			if (c.availableSlots() == topAvail) top.add(c);
			else break;
		}
		return top.get(ThreadLocalRandom.current().nextInt(top.size()));
	}

	private static void cleanup() {
		long now = Instant.now().toEpochMilli();
		serversByShortName.values().removeIf(s -> now - s.lastHeartbeat > HEARTBEAT_TTL_MS);
	}

	public record GameServerState(UUID uuid,
								  String shortName,
								  ServerType type,
								  Set<String> maps,
								  int maxPlayers,
								  int onlinePlayers,
								  long lastHeartbeat) {
		public int availableSlots() {
			return Math.max(0, maxPlayers - onlinePlayers);
		}
	}
}