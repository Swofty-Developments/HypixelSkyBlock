package net.swofty.type.generic.utility;

import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.orchestrator.GetGameCountsProtocolObject;
import net.swofty.proxyapi.ProxyService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared cache for game player counts.
 * Used by lobby NPCs to display per-mode player counts.
 */
public class GameCountCache {
    private static final Map<String, CachedCount> cache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL_MS = 5000; // 5 seconds

    /**
     * Get the player count for a specific server type and game type.
     * Returns cached value if fresh, otherwise triggers async refresh and returns stale/0.
     *
     * @param type The server type (e.g., MURDER_MYSTERY_GAME, BEDWARS_GAME)
     * @param gameTypeName The game mode name (e.g., "CLASSIC", "SOLO"), or null for all modes
     * @return The player count (may be stale during refresh)
     */
    public static int getPlayerCount(ServerType type, String gameTypeName) {
        return getPlayerCount(type, gameTypeName, null);
    }

    /**
     * Get the player count for a specific server type, game type, and map.
     *
     * @param type The server type (e.g., MURDER_MYSTERY_GAME, BEDWARS_GAME)
     * @param gameTypeName The game mode name (e.g., "CLASSIC", "SOLO"), or null for all modes
     * @param mapName The map name, or null for all maps
     * @return The player count (may be stale during refresh)
     */
    public static int getPlayerCount(ServerType type, String gameTypeName, String mapName) {
        String key = buildCacheKey(type, gameTypeName, mapName);
        CachedCount cached = cache.get(key);

        long now = System.currentTimeMillis();

        if (cached == null || now - cached.timestamp > CACHE_TTL_MS) {
            // Trigger async refresh
            refreshAsync(type, gameTypeName, mapName, key);

            // Return stale value if available, otherwise 0
            return cached != null ? cached.playerCount : 0;
        }

        return cached.playerCount;
    }

    /**
     * Get the game count for a specific server type and game type.
     *
     * @param type The server type
     * @param gameTypeName The game mode name, or null for all modes
     * @return The game count (may be stale during refresh)
     */
    public static int getGameCount(ServerType type, String gameTypeName) {
        return getGameCount(type, gameTypeName, null);
    }

    /**
     * Get the game count for a specific server type, game type, and map.
     *
     * @param type The server type
     * @param gameTypeName The game mode name, or null for all modes
     * @param mapName The map name, or null for all maps
     * @return The game count (may be stale during refresh)
     */
    public static int getGameCount(ServerType type, String gameTypeName, String mapName) {
        String key = buildCacheKey(type, gameTypeName, mapName);
        CachedCount cached = cache.get(key);

        long now = System.currentTimeMillis();

        if (cached == null || now - cached.timestamp > CACHE_TTL_MS) {
            // Trigger async refresh
            refreshAsync(type, gameTypeName, mapName, key);

            // Return stale value if available, otherwise 0
            return cached != null ? cached.gameCount : 0;
        }

        return cached.gameCount;
    }

    private static String buildCacheKey(ServerType type, String gameTypeName, String mapName) {
        StringBuilder key = new StringBuilder(type.name());
        key.append(":").append(gameTypeName != null ? gameTypeName : "ALL");
        key.append(":").append(mapName != null ? mapName : "ALL");
        return key.toString();
    }

    private static void refreshAsync(ServerType type, String gameTypeName, String mapName, String cacheKey) {
        var message = new GetGameCountsProtocolObject.GetGameCountsMessage(type, gameTypeName, mapName);

        new ProxyService(ServiceType.ORCHESTRATOR)
                .<GetGameCountsProtocolObject.GetGameCountsMessage, GetGameCountsProtocolObject.GetGameCountsResponse>handleRequest(message)
                .thenAccept(response -> {
                    if (response != null) {
                        cache.put(cacheKey, new CachedCount(
                                response.playerCount(),
                                response.gameCount(),
                                System.currentTimeMillis()
                        ));
                    }
                });
    }

    private record CachedCount(int playerCount, int gameCount, long timestamp) {}
}
