package net.swofty.commons.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisConnectionPool {

    public static JedisPool connect(String redisUri, Settings settings) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(settings.maxTotal());
        poolConfig.setMaxIdle(settings.maxIdle());
        poolConfig.setMinIdle(settings.minIdle());
        poolConfig.setMaxWait(settings.maxWait());
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setBlockWhenExhausted(false);

        JedisPool pool = new JedisPool(poolConfig, URI.create(redisUri));
        try (Jedis jedis = pool.getResource()) {
            jedis.ping();
        }
        return pool;
    }

    public record Settings(int maxTotal, int maxIdle, int minIdle, Duration maxWait) {
        public static Settings standard() {
            return new Settings(20, 5, 1, Duration.ofSeconds(2));
        }
    }
}
