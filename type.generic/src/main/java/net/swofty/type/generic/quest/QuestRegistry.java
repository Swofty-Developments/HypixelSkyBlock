package net.swofty.type.generic.quest;

import net.swofty.commons.YamlFileUtils;
import net.swofty.type.generic.achievement.AchievementCategory;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class QuestRegistry {
    private static final Map<String, QuestDefinition> QUESTS = new ConcurrentHashMap<>();
    private static final Map<AchievementCategory, List<QuestDefinition>> BY_CATEGORY = new ConcurrentHashMap<>();
    private static final Map<String, List<QuestDefinition>> BY_TRIGGER = new ConcurrentHashMap<>();

    private static boolean initialized = false;

    public static void loadFromConfiguration() {
        QUESTS.clear();
        BY_CATEGORY.clear();
        BY_TRIGGER.clear();

        File questsDir = new File("./configuration/quests");
        if (!questsDir.exists()) {
            Logger.warn("Quests configuration directory not found: {}", questsDir.getAbsolutePath());
            YamlFileUtils.ensureDirectoryExists(questsDir);
            initialized = true;
            return;
        }

        try {
            List<File> yamlFiles = YamlFileUtils.getYamlFiles(questsDir);
            for (File file : yamlFiles) {
                loadQuestsFromFile(file);
            }
            Logger.info("Loaded {} quests from configuration", QUESTS.size());
        } catch (IOException e) {
            Logger.error(e, "Failed to load quests from configuration");
        }

        initialized = true;
    }

    @SuppressWarnings("unchecked")
    private static void loadQuestsFromFile(File file) {
        try {
            Map<String, Object> yaml = YamlFileUtils.loadYaml(file);
            if (yaml == null || !yaml.containsKey("quests")) return;

            String parentFolder = file.getParentFile().getName();
            AchievementCategory category = AchievementCategory.fromConfigKey(parentFolder);
            if (category == null) {
                Logger.warn("Unknown category folder for quests: {}", parentFolder);
                return;
            }

            List<Map<String, Object>> questsList = (List<Map<String, Object>>) yaml.get("quests");
            for (Map<String, Object> questData : questsList) {
                QuestDefinition def = parseQuest(questData, category);
                if (def != null) {
                    registerQuest(def);
                }
            }
        } catch (IOException e) {
            Logger.error(e, "Failed to load quests from file: {}", file.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private static QuestDefinition parseQuest(Map<String, Object> data, AchievementCategory category) {
        try {
            String id = (String) data.get("id");
            String name = (String) data.get("name");
            String description = (String) data.get("description");
            String typeStr = (String) data.get("type");
            QuestType type = QuestType.valueOf(typeStr.toUpperCase());

            QuestReward.QuestRewardBuilder rewardBuilder = QuestReward.builder();
            if (data.containsKey("rewards")) {
                Map<String, Object> rewards = (Map<String, Object>) data.get("rewards");
                if (rewards.containsKey("hypixel_experience")) {
                    rewardBuilder.hypixelExperience(((Number) rewards.get("hypixel_experience")).longValue());
                }
                if (rewards.containsKey("game_experience")) {
                    rewardBuilder.gameExperience(((Number) rewards.get("game_experience")).longValue());
                }
                if (rewards.containsKey("coins")) {
                    rewardBuilder.coins(((Number) rewards.get("coins")).longValue());
                }
                if (rewards.containsKey("tokens")) {
                    rewardBuilder.tokens(((Number) rewards.get("tokens")).longValue());
                }
                if (rewards.containsKey("event_experience")) {
                    rewardBuilder.eventExperience(((Number) rewards.get("event_experience")).longValue());
                }
            }

            String headTexture = data.containsKey("head_texture") ? (String) data.get("head_texture") : null;

            return QuestDefinition.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .type(type)
                    .category(category)
                    .trigger((String) data.get("trigger"))
                    .goal(((Number) data.get("goal")).intValue())
                    .reward(rewardBuilder.build())
                    .headTexture(headTexture)
                    .build();
        } catch (Exception e) {
            Logger.error(e, "Failed to parse quest: {}", data);
            return null;
        }
    }

    private static void registerQuest(QuestDefinition def) {
        QUESTS.put(def.getId(), def);

        BY_CATEGORY.computeIfAbsent(def.getCategory(), k -> new ArrayList<>()).add(def);

        if (def.getTrigger() != null) {
            BY_TRIGGER.computeIfAbsent(def.getTrigger(), k -> new ArrayList<>()).add(def);
        }
    }

    public static QuestDefinition get(String id) {
        return QUESTS.get(id);
    }

    public static Collection<QuestDefinition> getAll() {
        return Collections.unmodifiableCollection(QUESTS.values());
    }

    public static List<QuestDefinition> getByCategory(AchievementCategory category) {
        return BY_CATEGORY.getOrDefault(category, Collections.emptyList());
    }

    public static List<QuestDefinition> getByCategory(AchievementCategory category, QuestType type) {
        return getByCategory(category).stream()
                .filter(q -> q.getType() == type)
                .collect(Collectors.toList());
    }

    public static List<QuestDefinition> getByTrigger(String trigger) {
        return BY_TRIGGER.getOrDefault(trigger, Collections.emptyList());
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
