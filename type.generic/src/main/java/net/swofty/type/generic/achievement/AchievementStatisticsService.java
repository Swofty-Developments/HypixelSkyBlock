package net.swofty.type.generic.achievement;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import org.bson.Document;
import org.tinylog.Logger;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AchievementStatisticsService {

    private static final Map<String, Long> UNLOCK_CACHE = new ConcurrentHashMap<>();
    private static long totalPlayerCount = 0;
    private static boolean initialized = false;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void initialize() {
        if (initialized) return;
        initialized = true;

        Thread.startVirtualThread(AchievementStatisticsService::runAggregation);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            try {
                runAggregation();
            } catch (Exception e) {
                Logger.error("Error running achievement statistics aggregation: " + e.getMessage());
            }
        }).delay(TaskSchedule.minutes(15))
          .repeat(TaskSchedule.minutes(15))
          .schedule();

        Logger.info("Achievement Statistics Service initialized");
    }

    public static double getUnlockPercentage(String achievementId, int tier) {
        if (totalPlayerCount == 0) return -1;

        String cacheKey = buildCacheKey(achievementId, tier);
        Long unlockCount = UNLOCK_CACHE.get(cacheKey);

        if (unlockCount == null) return 0.0;

        return (unlockCount * 100.0) / totalPlayerCount;
    }

    public static double getUnlockPercentage(String achievementId) {
        return getUnlockPercentage(achievementId, 0);
    }

    public static String getFormattedPercentage(String achievementId, int tier) {
        double percentage = getUnlockPercentage(achievementId, tier);
        if (percentage < 0) return "?.??";
        return String.format("%.2f", percentage);
    }

    public static String getFormattedPercentage(String achievementId) {
        return getFormattedPercentage(achievementId, 0);
    }

    public static void runAggregation() {
        Logger.info("Running achievement statistics aggregation...");
        long startTime = System.currentTimeMillis();

        try {
            totalPlayerCount = UserDatabase.collection.countDocuments();

            if (totalPlayerCount == 0) {
                Logger.info("No players in database, skipping aggregation");
                return;
            }

            aggregateAchievementUnlocks();

            long duration = System.currentTimeMillis() - startTime;
            Logger.info("Achievement statistics aggregation complete in {}ms. {} achievements tracked, {} total players",
                    duration, UNLOCK_CACHE.size(), totalPlayerCount);

        } catch (Exception e) {
            Logger.error(e, "Failed to run achievement statistics aggregation");
        }
    }

    private static void aggregateAchievementUnlocks() {
        Map<String, Long> newCounts = new ConcurrentHashMap<>();

        for (Document doc : UserDatabase.collection.find()) {
            String achievementDataJson = doc.getString("achievement_data");
            if (achievementDataJson == null || achievementDataJson.isEmpty()) continue;

            try {
                countPlayerAchievements(achievementDataJson, newCounts);
            } catch (Exception e) {
                Logger.debug("Failed to parse achievement data for player: {}", doc.getString("_id"));
            }
        }

        UNLOCK_CACHE.clear();
        UNLOCK_CACHE.putAll(newCounts);
    }

    private static void countPlayerAchievements(String achievementDataJson, Map<String, Long> counts) {
        try {
            JsonNode root = MAPPER.readTree(achievementDataJson);
            JsonNode achievements = root.get("achievements");

            if (achievements == null || !achievements.isObject()) return;

            achievements.properties().forEach(entry -> {
                String achievementId = entry.getKey();
                JsonNode progress = entry.getValue();

                AchievementDefinition def = AchievementRegistry.get(achievementId);
                if (def == null) return;

                if (def.getType() == AchievementType.TIERED) {
                    int currentTier = progress.has("currentTier") ? progress.get("currentTier").asInt() : 0;
                    for (int tier = 1; tier <= currentTier; tier++) {
                        String key = buildCacheKey(achievementId, tier);
                        counts.merge(key, 1L, Long::sum);
                    }
                } else {
                    boolean completed = progress.has("completed") && progress.get("completed").asBoolean();
                    if (completed) {
                        String key = buildCacheKey(achievementId, 0);
                        counts.merge(key, 1L, Long::sum);
                    }
                }
            });
        } catch (Exception _) {
            Logger.warn("Error parsing achievement data JSON");
        }
    }

    private static String buildCacheKey(String achievementId, int tier) {
        if (tier > 0) {
            return achievementId + ":" + tier;
        }
        return achievementId;
    }

    public static void forceRefresh() {
        Thread.startVirtualThread(AchievementStatisticsService::runAggregation);
    }

    public static long getTotalPlayerCount() {
        return totalPlayerCount;
    }
}
