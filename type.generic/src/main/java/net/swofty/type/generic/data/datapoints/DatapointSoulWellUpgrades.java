package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointSoulWellUpgrades extends Datapoint<DatapointSoulWellUpgrades.SoulWellUpgrades> {

    public DatapointSoulWellUpgrades(String key, SoulWellUpgrades value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(SoulWellUpgrades value) {
                JSONObject json = new JSONObject();
                JSONObject levelsJson = new JSONObject();
                for (Map.Entry<String, Integer> entry : value.getUpgradeLevels().entrySet()) {
                    levelsJson.put(entry.getKey(), entry.getValue());
                }
                json.put("upgradeLevels", levelsJson);
                json.put("wheelCount", value.getWheelCount());
                return json.toString();
            }

            @Override
            public SoulWellUpgrades deserialize(String json) {
                if (json == null || json.isEmpty()) {
                    return SoulWellUpgrades.empty();
                }
                JSONObject obj = new JSONObject(json);

                Map<String, Integer> upgradeLevels = new HashMap<>();
                JSONObject levelsObj = obj.optJSONObject("upgradeLevels");
                if (levelsObj != null) {
                    for (String key : levelsObj.keySet()) {
                        upgradeLevels.put(key, levelsObj.getInt(key));
                    }
                }

                int wheelCount = obj.optInt("wheelCount", 3);

                return new SoulWellUpgrades(upgradeLevels, wheelCount);
            }

            @Override
            public SoulWellUpgrades clone(SoulWellUpgrades value) {
                return value.copy();
            }
        });
    }

    public DatapointSoulWellUpgrades(String key) {
        this(key, SoulWellUpgrades.empty());
    }

    public static class SoulWellUpgrades {
        private final Map<String, Integer> upgradeLevels;
        private int wheelCount;

        public SoulWellUpgrades(Map<String, Integer> upgradeLevels, int wheelCount) {
            this.upgradeLevels = new HashMap<>(upgradeLevels);
            this.wheelCount = wheelCount;
        }

        public SoulWellUpgrades(Map<String, Integer> upgradeLevels) {
            this(upgradeLevels, 3); // Default to 3 wheels
        }

        public static SoulWellUpgrades empty() {
            return new SoulWellUpgrades(new HashMap<>(), 3);
        }

        public Map<String, Integer> getUpgradeLevels() {
            return upgradeLevels;
        }

        public int getWheelCount() {
            return wheelCount;
        }

        public void setWheelCount(int wheelCount) {
            this.wheelCount = Math.max(1, Math.min(5, wheelCount)); // Clamp between 1 and 5
        }

        /**
         * Get the level of a specific upgrade (0 if not purchased)
         */
        public int getUpgradeLevel(String upgradeId) {
            return upgradeLevels.getOrDefault(upgradeId, 0);
        }

        /**
         * Set the level of an upgrade
         */
        public void setUpgradeLevel(String upgradeId, int level) {
            upgradeLevels.put(upgradeId, level);
        }

        /**
         * Increment the level of an upgrade
         */
        public void incrementUpgradeLevel(String upgradeId) {
            int currentLevel = getUpgradeLevel(upgradeId);
            upgradeLevels.put(upgradeId, currentLevel + 1);
        }

        /**
         * Check if an upgrade has been purchased at least once
         */
        public boolean hasUpgrade(String upgradeId) {
            return upgradeLevels.containsKey(upgradeId) && upgradeLevels.get(upgradeId) > 0;
        }

        public SoulWellUpgrades copy() {
            return new SoulWellUpgrades(upgradeLevels, wheelCount);
        }
    }
}
