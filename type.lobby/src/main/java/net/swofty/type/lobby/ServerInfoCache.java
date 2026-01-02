package net.swofty.type.lobby;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.proxyapi.ProxyInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A shared cache for server information with 5-second TTL.
 * Used by game menus, lobby selectors, and NPCs to get player counts.
 */
public class ServerInfoCache {
    private static List<UnderstandableProxyServer> cachedServers = new ArrayList<>();
    private static long lastCacheTime = 0;
    private static final long CACHE_TTL_MS = 5000;

    /**
     * Get all servers, using cache if available.
     */
    public static CompletableFuture<List<UnderstandableProxyServer>> getServers() {
        if (!isCacheStale()) {
            return CompletableFuture.completedFuture(new ArrayList<>(cachedServers));
        }
        return refreshCache();
    }

    /**
     * Get servers of a specific type.
     */
    public static CompletableFuture<List<UnderstandableProxyServer>> getServersByType(ServerType type) {
        return getServers().thenApply(servers ->
                servers.stream().filter(s -> s.type() == type).toList()
        );
    }

    /**
     * Get total player count for a server type (uses cached data).
     */
    public static int getTotalPlayersForType(ServerType type) {
        return cachedServers.stream()
                .filter(s -> s.type() == type)
                .mapToInt(s -> s.players().size())
                .sum();
    }

    /**
     * Get total player count across all SkyBlock server types.
     */
    public static int getTotalSkyBlockPlayers() {
        return cachedServers.stream()
                .filter(s -> s.type().isSkyBlock())
                .mapToInt(s -> s.players().size())
                .sum();
    }

    /**
     * Force refresh the cache.
     */
    public static CompletableFuture<List<UnderstandableProxyServer>> refreshCache() {
        ProxyInformation info = new ProxyInformation();
        return info.getAllServersInformation().thenApply(servers -> {
            cachedServers = new ArrayList<>(servers);
            lastCacheTime = System.currentTimeMillis();
            return servers;
        });
    }

    /**
     * Check if cache is stale.
     */
    public static boolean isCacheStale() {
        return System.currentTimeMillis() - lastCacheTime >= CACHE_TTL_MS || cachedServers.isEmpty();
    }
}
