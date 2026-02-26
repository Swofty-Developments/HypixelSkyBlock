package net.swofty.service.punishment;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import net.swofty.commons.proxy.ToProxyChannels;
import org.json.JSONObject;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProxyRedis {
    private static JedisPool jedisPool;
    private static volatile boolean initialized = false;
    private static volatile boolean connecting = false;

    public static void connect(String redisUri) {
        Thread.startVirtualThread(() -> connectSync(redisUri));
    }

    private static synchronized void connectSync(String redisUri) {
        if (initialized || connecting) return;
        connecting = true;

        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(5);
            poolConfig.setMaxIdle(1);
            poolConfig.setMinIdle(1);
            poolConfig.setMaxWait(Duration.ofSeconds(2));
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestWhileIdle(true);
            poolConfig.setBlockWhenExhausted(false);

            URI uri = URI.create(redisUri);
            jedisPool = new JedisPool(poolConfig, uri);

            try (Jedis jedis = jedisPool.getResource()) {
                jedis.ping();
            }

            initialized = true;
            Logger.info("ProxyRedis: connected to Redis");
        } catch (Exception e) {
            Logger.error("ProxyRedis: Redis not available player ban enforcement unavailable");
            initialized = false;
            jedisPool = null;
        } finally {
            connecting = false;
        }
    }

    public static CompletableFuture<Void> publishMessage(String filterId, String channel, String message) {
        return CompletableFuture.runAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish(channel, filterId + ";" + message);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to publish message to Redis", ex);
            }
        });
    }

    public static void publishToProxy(ToProxyChannels channel, JSONObject message) {
        UUID uuid = UUID.randomUUID();
        publishMessage("proxy", channel.getChannelName(),
                message.toString() + "}=-=-={" + uuid + "}=-=-={" + uuid);
    }

    public static boolean isInitialized() {
        return initialized && jedisPool != null && !jedisPool.isClosed();
    }
}