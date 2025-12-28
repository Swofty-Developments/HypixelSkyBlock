package net.swofty.type.generic.experience;

import net.swofty.commons.YamlFileUtils;

import java.io.File;
import java.util.*;

public class LevelRewardRegistry {
    private static final Map<Integer, LevelReward> REWARDS = new TreeMap<>();
    private static boolean initialized = false;

    public static void initialize(File configRoot) {
        if (initialized) return;

        File rewardsFile = new File(configRoot, "leveling/rewards.yml");
        if (!rewardsFile.exists()) {
            System.out.println("[LevelRewardRegistry] rewards.yml not found at: " + rewardsFile.getAbsolutePath());
            initialized = true;
            return;
        }

        Map<String, Object> yaml;
        try {
            yaml = YamlFileUtils.loadYaml(rewardsFile);
        } catch (java.io.IOException e) {
            System.out.println("[LevelRewardRegistry] Failed to load rewards.yml: " + e.getMessage());
            initialized = true;
            return;
        }
        if (yaml == null) {
            System.out.println("[LevelRewardRegistry] Failed to load rewards.yml");
            initialized = true;
            return;
        }

        loadRewards(yaml);
        initialized = true;
        System.out.println("[LevelRewardRegistry] Loaded " + REWARDS.size() + " level rewards");
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
                System.err.println("[LevelRewardRegistry] Error loading reward: " + e.getMessage());
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
