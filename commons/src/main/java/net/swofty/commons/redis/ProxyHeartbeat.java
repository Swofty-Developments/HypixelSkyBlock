package net.swofty.commons.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Liveness signalling between the Velocity proxy and the game servers.
 *
 * <p>The old design had every game server send a {@code ProxyIsOnlineProtocol}
 * request/response to the proxy once per second and shut down on a single missed
 * reply within ~1s. Under many concurrent servers that round-trip regularly
 * exceeded the window and killed healthy servers.
 *
 * <p>This replaces it with a push/expiry model: the proxy refreshes a single
 * short-lived Redis key on a fixed interval (one write regardless of how many
 * servers exist). Game servers simply read the key — if it is absent (the proxy
 * stopped refreshing it and Redis let it expire) for several consecutive checks,
 * the proxy is considered dead. No per-server request/response, no coupling to
 * the game tick loop, and transient Redis latency is harmless.
 */
public final class ProxyHeartbeat {
    private static final String KEY = "proxy:heartbeat";
    /** How long a single beat keeps the key alive. Must exceed the beat interval. */
    private static final int TTL_SECONDS = 6;

    private static volatile JedisPool pool;

    private ProxyHeartbeat() {}

    public static synchronized void init(String redisUri) {
        if (pool == null) {
            pool = RedisConnectionPool.connect(redisUri, RedisConnectionPool.Settings.standard());
        }
    }

    /** Proxy: call on a fixed interval shorter than {@link #TTL_SECONDS} to advertise liveness. */
    public static void beat() {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(KEY, TTL_SECONDS, Long.toString(System.currentTimeMillis()));
        }
    }

    /** Game server: true while the proxy is refreshing its heartbeat key. */
    public static boolean isProxyAlive() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(KEY);
        }
    }
}
