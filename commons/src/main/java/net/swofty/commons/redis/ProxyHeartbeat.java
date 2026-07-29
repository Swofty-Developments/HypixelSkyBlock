package net.swofty.commons.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public final class ProxyHeartbeat {
    private static final String KEY = "proxy:heartbeat";
    private static final int TTL_SECONDS = 6;

    private static volatile JedisPool pool;

    private ProxyHeartbeat() {}

    public static synchronized void init(String redisUri) {
        if (pool == null) {
            pool = RedisConnectionPool.connect(redisUri, RedisConnectionPool.Settings.standard());
        }
    }

    public static void beat() {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(KEY, TTL_SECONDS, Long.toString(System.currentTimeMillis()));
        }
    }

    public static boolean isProxyAlive() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(KEY);
        }
    }
}
