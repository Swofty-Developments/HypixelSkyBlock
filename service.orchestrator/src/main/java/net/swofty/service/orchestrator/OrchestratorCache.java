package net.swofty.service.orchestrator;

import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.commons.protocol.objects.orchestrator.GameHeartbeatProtocolObject;
import net.swofty.type.game.game.GameObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class OrchestratorCache {
    private static final Map<String, GameServerState> serversByShortName = new ConcurrentHashMap<>();
    private static final Map<UUID, GameWithServer> gamesByGameId = new ConcurrentHashMap<>();
    private static final long HEARTBEAT_TTL_MS = 10000; // 10s

    public static void handleHeartbeat(UUID uuid,
                                       String shortName,
                                       ServerType type,
                                       int maxPlayers,
                                       int onlinePlayers,
                                       List<GameObject> games) {
        handleHeartbeat(uuid, shortName, type, maxPlayers, onlinePlayers, games, List.of(), null);
    }

    public static void handleHeartbeat(UUID uuid,
                                       String shortName,
                                       ServerType type,
                                       int maxPlayers,
                                       int onlinePlayers,
                                       List<GameObject> games,
                                       List<GameHeartbeatProtocolObject.MapAdvertisement> mapAdvertisements,
                                       Integer remainingGameSlots) {
        GameServerState state = new GameServerState(
            uuid,
            shortName,
            type,
            maxPlayers,
            onlinePlayers,
            Instant.now().toEpochMilli(),
            mapAdvertisements != null ? List.copyOf(mapAdvertisements) : List.of(),
            remainingGameSlots
        );
        serversByShortName.put(shortName, state);

        // Remove old games from this server
        gamesByGameId.entrySet().removeIf(entry -> entry.getValue().serverUuid().equals(uuid));

        // Add current games with server tracking
        for (GameObject game : games) {
            gamesByGameId.put(game.getGameId(), new GameWithServer(game, uuid, shortName));
        }
    }

    /**
     * Finds an existing joinable game for the specified game type and map (Bedwars-specific)
     */
    public static GameWithServer findExisting(BedWarsGameType gameType, String map) {
        return findExisting(ServerType.BEDWARS_GAME, gameType.maxPlayers(), map, 1, gameType.name());
    }

    /**
     * Finds an existing joinable game for the specified game type and map with needed slots (Bedwars-specific)
     */
    public static GameWithServer findExisting(BedWarsGameType gameType, String map, int neededSlots) {
        return findExisting(ServerType.BEDWARS_GAME, gameType.maxPlayers(), map, neededSlots, gameType.name());
    }

    public static GameWithServer findMostPopulatedBedwarsGame(BedWarsGameType gameType, int neededSlots) {
        return findExisting(ServerType.BEDWARS_GAME, gameType.maxPlayers(), null, neededSlots, gameType.name(), true);
    }

    /**
     * Generic version - finds an existing joinable game for any server type
     */
    public static GameWithServer findExisting(ServerType serverType, int maxPlayers, String map) {
        return findExisting(serverType, maxPlayers, map, 1);
    }

    /**
     * Finds an existing joinable game with at least neededSlots available slots
     */
    public static GameWithServer findExisting(ServerType serverType, int maxPlayers, String map, int neededSlots) {
        return findExisting(serverType, maxPlayers, map, neededSlots, null);
    }

    /**
     * Finds an existing joinable game with at least neededSlots available slots, filtered by game type name
     */
    public static GameWithServer findExisting(ServerType serverType, int maxPlayers, String map, int neededSlots, String gameTypeName) {
        return findExisting(serverType, maxPlayers, map, neededSlots, gameTypeName, false);
    }

    /**
     * Finds an existing joinable game with at least neededSlots available slots.
     * If requirePlayers is true, only considers games that already have players.
     */
    public static GameWithServer findExisting(ServerType serverType,
                                              int maxPlayers,
                                              String map,
                                              int neededSlots,
                                              String gameTypeName,
                                              boolean requirePlayers) {
        cleanup();

        List<GameWithServer> candidates = new ArrayList<>();
        for (GameWithServer gameWithServer : gamesByGameId.values()) {
            GameObject game = gameWithServer.game();
            int availableSlots = maxPlayers - game.getInvolvedPlayers().size();
            if (game.getType() == serverType &&
                availableSlots >= neededSlots &&
                (map == null || game.getMap().equals(map)) &&
                (gameTypeName == null || gameTypeName.equals(game.getGameTypeName())) &&
                (!requirePlayers || !game.getInvolvedPlayers().isEmpty())) {
                candidates.add(gameWithServer);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        // Prefer games with more players (closer to starting)
        candidates.sort(Comparator.comparingInt((GameWithServer g) -> g.game().getInvolvedPlayers().size()).reversed());
        return candidates.getFirst();
    }

    /**
     * Finds a server with capacity to host a new game (Bedwars-specific)
     */
    public static GameServerState instantiateServer(BedWarsGameType gameType, String map) {
        cleanup();

        List<GameServerState> candidates = new ArrayList<>();
        for (GameServerState server : serversByShortName.values()) {
            if (server.type() != ServerType.BEDWARS_GAME) {
                continue;
            }
            if (server.availableSlots() < gameType.maxPlayers()) {
                continue;
            }
            if (server.remainingGameSlots() != null && server.remainingGameSlots() <= 0) {
                continue;
            }
            if (!supportsBedwarsRequest(server, map, gameType.name())) {
                continue;
            }
            candidates.add(server);
        }

        if (candidates.isEmpty()) {
            return null;
        }

        candidates.sort(Comparator
            .comparingInt(GameServerState::availableSlots)
            .thenComparingInt(server -> server.remainingGameSlots() != null ? server.remainingGameSlots() : Integer.MAX_VALUE)
            .reversed());

        GameServerState best = candidates.getFirst();
        List<GameServerState> topServers = candidates.stream()
            .filter(server -> server.availableSlots() == best.availableSlots())
            .filter(server -> {
                int serverSlots = server.remainingGameSlots() != null ? server.remainingGameSlots() : Integer.MAX_VALUE;
                int bestSlots = best.remainingGameSlots() != null ? best.remainingGameSlots() : Integer.MAX_VALUE;
                return serverSlots == bestSlots;
            })
            .toList();

        return topServers.get(ThreadLocalRandom.current().nextInt(topServers.size()));
    }

    /**
     * Generic version - finds a server with capacity to host a new game for any server type
     */
    public static GameServerState instantiateServer(ServerType serverType, int maxPlayers) {
        cleanup();

        List<GameServerState> candidates = new ArrayList<>();
        for (GameServerState server : serversByShortName.values()) {
            if (server.type() == serverType &&
                server.availableSlots() >= maxPlayers) {
                candidates.add(server);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        // Prefer servers with more available capacity
        candidates.sort(Comparator.comparingInt(GameServerState::availableSlots).reversed());
        int topAvail = candidates.getFirst().availableSlots();

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

        if (type == ServerType.BEDWARS_GAME) {
            Set<String> advertisedMaps = getAdvertisedBedwarsMapNames(gameTypeName);
            if (!advertisedMaps.isEmpty()) {
                return advertisedMaps;
            }
        }

        Set<String> maps = new HashSet<>();

        for (GameWithServer gameWithServer : gamesByGameId.values()) {
            GameObject game = gameWithServer.game();
            if (game.getType() == type && (gameTypeName == null || gameTypeName.equals(game.getGameTypeName()))) {
                maps.add(game.getMap());
            }
        }

        return maps;
    }

    public static List<String> getEligibleBedwarsMaps(BedWarsGameType gameType) {
        cleanup();
        Set<String> advertisedMaps = getAdvertisedBedwarsMapNames(gameType.name());
        if (!advertisedMaps.isEmpty()) {
            return advertisedMaps.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
        }

        Set<String> maps = new HashSet<>();
        for (GameWithServer gameWithServer : gamesByGameId.values()) {
            GameObject game = gameWithServer.game();
            if (game.getType() == ServerType.BEDWARS_GAME && gameType.name().equals(game.getGameTypeName())) {
                maps.add(game.getMap());
            }
        }
        return maps.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
    }

    public static String pickRandomBedwarsMap(BedWarsGameType gameType) {
        List<String> maps = getEligibleBedwarsMaps(gameType);
        if (maps.isEmpty()) {
            return null;
        }
        return maps.get(ThreadLocalRandom.current().nextInt(maps.size()));
    }

    /**
     * Find a game that a player is part of (either active or disconnected).
     * Used for the rejoin system.
     */
    public static GameWithServer findPlayerGame(UUID playerUuid) {
        cleanup();

        for (GameWithServer gameWithServer : gamesByGameId.values()) {
            GameObject game = gameWithServer.game();
            // Check active players
            if (game.getInvolvedPlayers().contains(playerUuid)) {
                return gameWithServer;
            }
            // Check disconnected players
            if (game.getDisconnectedPlayers() != null && game.getDisconnectedPlayers().contains(playerUuid)) {
                return gameWithServer;
            }
        }
        return null;
    }

    public static void cleanup() {
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
                                  long lastHeartbeat,
                                  List<GameHeartbeatProtocolObject.MapAdvertisement> mapAdvertisements,
                                  Integer remainingGameSlots) {

        public GameServerState {
            mapAdvertisements = mapAdvertisements != null ? List.copyOf(mapAdvertisements) : List.of();
        }

        public int availableSlots() {
            return Math.max(0, maxPlayers - onlinePlayers);
        }
    }

    public record GameWithServer(GameObject game, UUID serverUuid, String serverShortName) {
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
        cleanup();
        return serversByShortName.values().stream()
            .filter(server -> server.uuid().equals(serverUuid))
            .findFirst()
            .orElse(null);
    }

    /**
     * Get player and game counts filtered by server type and optionally by game type name.
     *
     * @param type         The server type (e.g., MURDER_MYSTERY_GAME, BEDWARS_GAME)
     * @param gameTypeName Optional game type name to filter by (e.g., "CLASSIC", "SOLO"). Pass null for all.
     * @return GameCountStats with player count and game count
     */
    public static GameCountStats getGameCounts(ServerType type, String gameTypeName) {
        return getGameCounts(type, gameTypeName, null);
    }

    /**
     * Get player and game counts filtered by server type, game type name, and map name.
     *
     * @param type         The server type (e.g., MURDER_MYSTERY_GAME, BEDWARS_GAME)
     * @param gameTypeName Optional game type name to filter by (e.g., "CLASSIC", "SOLO"). Pass null for all.
     * @param mapName      Optional map name to filter by. Pass null for all maps.
     * @return GameCountStats with player count and game count
     */
    public static GameCountStats getGameCounts(ServerType type, String gameTypeName, String mapName) {
        cleanup();
        int playerCount = 0;
        int gameCount = 0;

        for (GameWithServer gameWithServer : gamesByGameId.values()) {
            GameObject game = gameWithServer.game();
            if (game.getType() == type) {
                if (gameTypeName == null || gameTypeName.equals(game.getGameTypeName())) {
                    if (mapName == null || mapName.equals(game.getMap())) {
                        playerCount += game.getInvolvedPlayers().size();
                        gameCount++;
                    }
                }
            }
        }

        return new GameCountStats(playerCount, gameCount);
    }

    public record GameCountStats(int playerCount, int gameCount) {
    }

    private static Set<String> getAdvertisedBedwarsMapNames(String gameTypeName) {
        Set<String> maps = new HashSet<>();
        for (GameServerState server : serversByShortName.values()) {
            if (server.type() != ServerType.BEDWARS_GAME || server.mapAdvertisements().isEmpty()) {
                continue;
            }
            for (GameHeartbeatProtocolObject.MapAdvertisement advertisement : server.mapAdvertisements()) {
                if (supportsMode(advertisement, gameTypeName)) {
                    maps.add(advertisement.mapName());
                }
            }
        }
        return maps;
    }

    private static boolean supportsBedwarsRequest(GameServerState server, String map, String gameTypeName) {
        if (server.mapAdvertisements().isEmpty()) {
            return true;
        }

        if (map == null) {
            for (GameHeartbeatProtocolObject.MapAdvertisement advertisement : server.mapAdvertisements()) {
                if (supportsMode(advertisement, gameTypeName)) {
                    return true;
                }
            }
            return false;
        }

        for (GameHeartbeatProtocolObject.MapAdvertisement advertisement : server.mapAdvertisements()) {
            if (matchesMap(advertisement, map) && supportsMode(advertisement, gameTypeName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchesMap(GameHeartbeatProtocolObject.MapAdvertisement advertisement, String requestedMap) {
        return advertisement.mapId().equalsIgnoreCase(requestedMap)
            || advertisement.mapName().equalsIgnoreCase(requestedMap);
    }

    private static boolean supportsMode(GameHeartbeatProtocolObject.MapAdvertisement advertisement, String gameTypeName) {
        if (gameTypeName == null) {
            return true;
        }
        for (String mode : advertisement.supportedModes()) {
            if (mode.equalsIgnoreCase(gameTypeName)) {
                return true;
            }
        }
        return false;
    }
}