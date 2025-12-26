package net.swofty.type.generic.achievement;

import net.swofty.commons.YamlFileUtils;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AchievementRegistry {
    private static final Map<String, AchievementDefinition> ACHIEVEMENTS = new ConcurrentHashMap<>();
    private static final Map<AchievementCategory, List<AchievementDefinition>> BY_CATEGORY = new ConcurrentHashMap<>();
    private static final Map<String, List<AchievementDefinition>> BY_TRIGGER = new ConcurrentHashMap<>();

    private static boolean initialized = false;

    public static void loadFromConfiguration() {
        ACHIEVEMENTS.clear();
        BY_CATEGORY.clear();
        BY_TRIGGER.clear();

        File achievementsDir = new File("./configuration/achievements");
        if (!achievementsDir.exists()) {
            Logger.warn("Achievements configuration directory not found: {}", achievementsDir.getAbsolutePath());
            YamlFileUtils.ensureDirectoryExists(achievementsDir);
            initialized = true;
            return;
        }

        try {
            List<File> yamlFiles = YamlFileUtils.getYamlFiles(achievementsDir);
            for (File file : yamlFiles) {
                loadAchievementsFromFile(file);
            }
            Logger.info("Loaded {} achievements from configuration", ACHIEVEMENTS.size());
        } catch (IOException e) {
            Logger.error(e, "Failed to load achievements from configuration");
        }

        initialized = true;
    }

    @SuppressWarnings("unchecked")
    private static void loadAchievementsFromFile(File file) {
        try {
            Map<String, Object> yaml = YamlFileUtils.loadYaml(file);
            if (yaml == null || !yaml.containsKey("achievements")) return;

            String parentFolder = file.getParentFile().getName();
            AchievementCategory category = AchievementCategory.fromConfigKey(parentFolder);
            if (category == null) {
                Logger.warn("Unknown category folder: {}", parentFolder);
                return;
            }

            List<Map<String, Object>> achievementsList = (List<Map<String, Object>>) yaml.get("achievements");
            for (Map<String, Object> achievementData : achievementsList) {
                AchievementDefinition def = parseAchievement(achievementData, category);
                if (def != null) {
                    registerAchievement(def);
                }
            }
        } catch (IOException e) {
            Logger.error(e, "Failed to load achievements from file: {}", file.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private static AchievementDefinition parseAchievement(Map<String, Object> data, AchievementCategory category) {
        try {
            String id = (String) data.get("id");
            String name = (String) data.get("name");
            String description = (String) data.get("description");
            String typeStr = (String) data.get("type");
            AchievementType type = AchievementType.valueOf(typeStr.toUpperCase());

            AchievementDefinition.AchievementDefinitionBuilder builder = AchievementDefinition.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .type(type)
                    .category(category)
                    .trigger((String) data.get("trigger"))
                    .perGame(Boolean.TRUE.equals(data.get("perGame")))
                    .customCheck(Boolean.TRUE.equals(data.get("customCheck")));

            if (type == AchievementType.SEASONAL && data.containsKey("season")) {
                builder.season(SeasonType.fromString((String) data.get("season")));
            }

            if (data.containsKey("goal")) {
                builder.goal(((Number) data.get("goal")).intValue());
            }
            if (data.containsKey("points")) {
                builder.points(((Number) data.get("points")).intValue());
            }

            if (type == AchievementType.TIERED && data.containsKey("tiers")) {
                List<Map<String, Object>> tiersData = (List<Map<String, Object>>) data.get("tiers");
                List<AchievementTier> tiers = new ArrayList<>();
                for (Map<String, Object> tierData : tiersData) {
                    tiers.add(new AchievementTier(
                            ((Number) tierData.get("tier")).intValue(),
                            ((Number) tierData.get("goal")).intValue(),
                            ((Number) tierData.get("points")).intValue()
                    ));
                }
                builder.tiers(tiers);
            }

            return builder.build();
        } catch (Exception e) {
            Logger.error(e, "Failed to parse achievement: {}", data);
            return null;
        }
    }

    private static void registerAchievement(AchievementDefinition def) {
        ACHIEVEMENTS.put(def.getId(), def);

        BY_CATEGORY.computeIfAbsent(def.getCategory(), k -> new ArrayList<>()).add(def);

        if (def.getTrigger() != null) {
            BY_TRIGGER.computeIfAbsent(def.getTrigger(), k -> new ArrayList<>()).add(def);
        }
    }

    public static AchievementDefinition get(String id) {
        return ACHIEVEMENTS.get(id);
    }

    public static Collection<AchievementDefinition> getAll() {
        return Collections.unmodifiableCollection(ACHIEVEMENTS.values());
    }

    public static List<AchievementDefinition> getByCategory(AchievementCategory category) {
        return BY_CATEGORY.getOrDefault(category, Collections.emptyList());
    }

    public static List<AchievementDefinition> getByCategory(AchievementCategory category, AchievementType type) {
        return getByCategory(category).stream()
                .filter(a -> a.getType() == type)
                .collect(Collectors.toList());
    }

    public static List<AchievementDefinition> getByTrigger(String trigger) {
        return BY_TRIGGER.getOrDefault(trigger, Collections.emptyList());
    }

    public static List<AchievementDefinition> getBySeason(AchievementCategory category, SeasonType season) {
        return getByCategory(category, AchievementType.SEASONAL).stream()
                .filter(a -> a.getSeason() == season)
                .collect(Collectors.toList());
    }

    public static List<AchievementDefinition> getPerGameAchievements(String trigger) {
        return getByTrigger(trigger).stream()
                .filter(AchievementDefinition::isPerGame)
                .collect(Collectors.toList());
    }

    public static int getCount(AchievementCategory category) {
        return getByCategory(category).size();
    }

    public static int getCount(AchievementCategory category, AchievementType type) {
        return getByCategory(category, type).size();
    }

    public static int getTierCount(AchievementCategory category) {
        return getByCategory(category, AchievementType.TIERED).stream()
                .mapToInt(AchievementDefinition::getMaxTier)
                .sum();
    }

    public static int getTotalPoints(AchievementCategory category) {
        return getByCategory(category).stream()
                .mapToInt(AchievementDefinition::getTotalPoints)
                .sum();
    }

    public static int getTotalPoints(AchievementCategory category, AchievementType type) {
        return getByCategory(category, type).stream()
                .mapToInt(AchievementDefinition::getTotalPoints)
                .sum();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static int getTotalCount() {
        return ACHIEVEMENTS.size();
    }

    public static int getTotalMaxPoints() {
        return ACHIEVEMENTS.values().stream()
                .mapToInt(AchievementDefinition::getTotalPoints)
                .sum();
    }
}
