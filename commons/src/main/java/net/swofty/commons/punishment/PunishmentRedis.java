package net.swofty.commons.punishment;

import com.google.gson.Gson;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PunishmentRedis {
    private static final String PREFIX = "punish:active:";
    private static final Gson GSON = new Gson();
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
            poolConfig.setMaxTotal(20);
            poolConfig.setMaxIdle(5);
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
            Logger.info("PunishmentService: connected to Redis");
        } catch (Exception e) {
            Logger.warn("PunishmentService: Redis not available, punishments disabled");
            initialized = false;
            jedisPool = null;
        } finally {
            connecting = false;
        }
    }

    public static boolean isInitialized() {
        return initialized && jedisPool != null && !jedisPool.isClosed();
    }

    private static String key(UUID playerId, String type) {
        return PREFIX + playerId + ":" + type;
    }

    public static void saveActivePunishment(UUID playerId, String type, String id,
                                            PunishmentReason reason, long expiresAt,
                                            List<PunishmentTag> tags) {
        if (!isInitialized()) throw new IllegalStateException("PunishmentRedis not initialized");

        try (Jedis jedis = jedisPool.getResource()) {
            String redisKey = key(playerId, type);

            HashMap<String, String> data = new HashMap<>(Map.of(
                    "type", type,
                    "banId", id,
                    "reason", GSON.toJson(reason),
                    "expiresAt", String.valueOf(expiresAt)
            ));
            if (tags != null && !tags.isEmpty()) {
                data.put("tags", GSON.toJson(tags));
            }

            jedis.hset(redisKey, data);
            if (expiresAt > 0) {
                long ttlSeconds = (expiresAt - System.currentTimeMillis()) / 1000;
                if (ttlSeconds > 0) {
                    jedis.expire(redisKey, (int) ttlSeconds);
                }
            } else {
                jedis.persist(redisKey);
            }
        }
    }

    public static Optional<ActivePunishment> getActive(UUID playerId, String type) {
        if (!isInitialized()) return Optional.empty();

        try (Jedis jedis = jedisPool.getResource()) {
            String redisKey = key(playerId, type);
            Map<String, String> data = jedis.hgetAll(redisKey);

            if (data.isEmpty()) return Optional.empty();

            String banId = data.get("banId");
            long expiresAt = Long.parseLong(data.getOrDefault("expiresAt", "-1"));

            if (expiresAt > 0 && System.currentTimeMillis() > expiresAt) {
                jedis.del(redisKey);
                return Optional.empty();
            }

            PunishmentReason reason = GSON.fromJson(data.get("reason"), PunishmentReason.class);

            List<PunishmentTag> tags = List.of();
            String tagsJson = data.get("tags");
            if (tagsJson != null && !tagsJson.isBlank()) {
                tags = List.of(GSON.fromJson(tagsJson, PunishmentTag[].class));
            }

            return Optional.of(new ActivePunishment(type, banId, reason, expiresAt, tags));
        }
    }

    public static List<ActivePunishment> getAllActive(UUID playerId) {
        if (!isInitialized()) return List.of();

        List<ActivePunishment> result = new ArrayList<>();
        for (PunishmentType punishmentType : PunishmentType.values()) {
            getActive(playerId, punishmentType.name()).ifPresent(result::add);
        }
        return result;
    }

    public static void revoke(UUID playerId, String type) {
        if (!isInitialized()) throw new IllegalStateException("PunishmentRedis not initialized");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key(playerId, type));
        }
    }
}
