package net.swofty.commons.punishment;

import com.google.gson.Gson;
import net.kyori.adventure.text.Component;
import net.swofty.commons.StringUtility;
import org.tinylog.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PunishmentRedis {
    private static final String PREFIX = "punish:";
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

            // Test connection
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

    public static void saveActivePunishment(UUID playerId, String type, String id, PunishmentReason reason, long expiresAt) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = PREFIX + "active:" + playerId;

            Gson gson = new Gson();
            Map<String, String> data = Map.of(
                    "type", type,
                    "banId", id,
                    "reason", gson.toJson(reason), // most likely not optimal for performance
                    "expiresAt", String.valueOf(expiresAt)
            );

            jedis.hset(key, data);
            if (expiresAt > 0) {
                long ttlSeconds = (expiresAt - System.currentTimeMillis()) / 1000;
                if (ttlSeconds > 0) {
                    jedis.expire(key, (int) ttlSeconds);
                }
            }
        }
    }

    public static Optional<ActivePunishment> getActive(UUID playerId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = PREFIX + "active:" + playerId;
            Map<String, String> data = jedis.hgetAll(key);

            if (data.isEmpty()) return Optional.empty();

            String type = data.get("type");
            String banId = data.get("banId");
            long expiresAt = Long.parseLong(data.getOrDefault("expiresAt", "-1"));

            if (expiresAt > 0 && System.currentTimeMillis() > expiresAt) {
                jedis.del(key); // clean up expired
                return Optional.empty();
            }

            Gson gson = new Gson();
            PunishmentReason reason = gson.fromJson(data.get("reason"), PunishmentReason.class); // most likely not optimal for performance

            return Optional.of(new ActivePunishment(type, banId, reason, expiresAt));
        }
    }

    public static CompletableFuture<Long> revoke(UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = PREFIX + "active:" + playerId;
                return jedis.del(key);
            }
        });
    }

    public record ActivePunishment(String type, String banId, PunishmentReason reason, long expiresAt) {}

    public static Component parseActivePunishmentBanMessage(ActivePunishment punishment) {
        long expiresAt = punishment.expiresAt();
        PunishmentReason reason = punishment.reason();
        String banId = punishment.banId();

        long timeLeft = expiresAt - System.currentTimeMillis();
        String prettyTimeLeft = StringUtility.formatTimeLeft(timeLeft);

        String header;
        if (expiresAt <= 0) {
            header = "§cYou are permanently banned from this server!\n";
        } else {
            header = "§cYou are temporarily banned for §f" + prettyTimeLeft + " §cfrom this server!\n";
        }

        String findOutMore = "";
        if (reason.getBanType() != null && reason.getBanType().getUrl() != null) {
            findOutMore = "§7Find out more: §b" + reason.getBanType().getUrl() + "\n";
        }

        String footer = "§7Sharing your Ban ID may affect the processing of your appeal!";

        return Component.text(header + "\n§7Reason: §f" + reason.getReasonString() + "\n" + findOutMore + "\n§7Ban ID: §f" + banId + "\n" + footer);
    }

    public static Component parseActivePunishmentMuteMessage(ActivePunishment punishment) {
        long expiresAt = punishment.expiresAt();
        PunishmentReason reason = punishment.reason();

        long timeLeft = expiresAt - System.currentTimeMillis();
        String prettyTimeLeft = StringUtility.formatTimeLeft(timeLeft);

        String line = "\n§c§m                                                     §r\n";

        String header;
        if (expiresAt <= 0) {
            header = "§cYou are permanently muted on this server!\n";
        } else {
            header = "§cYou are currently muted for muted for " + reason.getReasonString() + "\n";
        }
        String time = "§7Your mute will expire in §c" + prettyTimeLeft + "\n\n";

        String urlInfo = "§7Find out more here: §fwww.hypixel.net/mutes\n";
        String footer = "§7Mute ID: §f" + punishment.banId();
        return Component.text(line + header + time + urlInfo + footer + line);
    }

    // lookup for all BANNED players
    public static CompletableFuture<Set<String>> getAllBannedPlayerIds() {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                Set<String> keys = jedis.keys(PREFIX + "active:*");
                return keys.stream()
                        .map(key -> key.substring((PREFIX + "active:").length()))
                        .collect(Collectors.toSet());
            }
        });
    }

}
