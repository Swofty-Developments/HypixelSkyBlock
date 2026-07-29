package net.swofty.type.generic.experience;

import net.swofty.commons.YamlFileUtils;
import org.tinylog.Logger;

import java.io.File;
import java.util.*;

public class LevelRewardRegistry {
    private static final Map<Integer, LevelReward> REWARDS = new TreeMap<>();
    private static boolean initialized = false;

    public static void initialize(File configRoot) {
        if (initialized) return;

        File rewardsFile = new File(configRoot, "leveling/rewards.yml");
        if (!rewardsFile.exists()) {
            Logger.warn("rewards.yml not found at: {}", rewardsFile.getAbsolutePath());
            initialized = true;
            return;
        }

        Map<String, Object> yaml;
        try {
            yaml = YamlFileUtils.loadYaml(rewardsFile);
        } catch (java.io.IOException e) {
            Logger.error(e, "Failed to load rewards.yml");
            initialized = true;
            return;
        }
        if (yaml == null) {
            Logger.warn("rewards.yml parsed to null — empty file?");
            initialized = true;
            return;
        }

        loadRewards(yaml);
        initialized = true;
        Logger.info("Loaded {} level rewards", REWARDS.size());
    }

    @SuppressWarnings("unchecked")
    private static void loadRewards(Map<String, Object> yaml) {
        Object rewardsObj = yaml.get("rewards");
        if (!(rewardsObj instanceof List)) return;

        List<Map<String, Object>> rewardsList = (List<Map<String, Object>>) rewardsObj;

        for (Map<String, Object> rewardData : rewardsList) {
            try {
                int level = getInt(rewardData, "level", 0);
                if (level <= 0) continue;

                LevelReward.LevelRewardBuilder builder = LevelReward.builder()
                        .level(level)
                        .coins(getInt(rewardData, "coins", 0))
                        .dust(getInt(rewardData, "dust", 0))
                        .mysteryDust(getInt(rewardData, "mysteryDust", 0))
                        .boosterType(getString(rewardData, "boosterType"))
                        .boosterDuration(getInt(rewardData, "boosterDuration", 0));

                Object specialObj = rewardData.get("specialRewards");
                if (specialObj instanceof List) {
                    builder.specialRewards((List<String>) specialObj);
                }

                LevelReward reward = builder.build();
                REWARDS.put(level, reward);
            } catch (Exception e) {
                Logger.error(e, "Error loading level reward entry");
            }
        }
    }

    private static int getInt(Map<String, Object> map, String key, int defaultValue) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }

    private static String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public static LevelReward get(int level) {
        return REWARDS.get(level);
    }

    public static List<LevelReward> getRewardsUpTo(int level) {
        List<LevelReward> result = new ArrayList<>();
        for (Map.Entry<Integer, LevelReward> entry : REWARDS.entrySet()) {
            if (entry.getKey() <= level) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public static List<LevelReward> getAllRewards() {
        return new ArrayList<>(REWARDS.values());
    }

    public static List<LevelReward> getRewardsInRange(int startLevel, int endLevel) {
        List<LevelReward> result = new ArrayList<>();
        for (int level = startLevel; level <= endLevel; level++) {
            LevelReward reward = REWARDS.get(level);
            if (reward != null) {
                result.add(reward);
            }
        }
        return result;
    }

    public static boolean hasReward(int level) {
        return REWARDS.containsKey(level);
    }

    public static int getRewardCount() {
        return REWARDS.size();
    }

    public static int getMaxRewardLevel() {
        return REWARDS.isEmpty() ? 0 : Collections.max(REWARDS.keySet());
    }
}
