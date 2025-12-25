package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.skill.SkillCategory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class DatapointSkills extends SkyBlockDatapoint<DatapointSkills.PlayerSkills> {
    private static final Serializer<PlayerSkills> serializer = new Serializer<>() {

        @Override
        public String serialize(PlayerSkills value) {
            JSONObject jsonObject = new JSONObject();

            for (SkillCategories category : SkillCategories.values()) {
                jsonObject.put(category.toString(), value.getRaw(category));
            }

            ItemStatistics statistics = value.skillStatistics;
            jsonObject.put("statistics", statistics.toString());

            return jsonObject.toString();
        }

        @Override
        public PlayerSkills deserialize(String json) {
            HashMap<SkillCategories, Double> skills = new HashMap<>(PlayerSkills.getDefault().getSkills());
            ItemStatistics skillStatistics = ItemStatistics.empty();

            if (json == null || json.isEmpty()) {
                return new PlayerSkills(skills, skillStatistics);
            }

            JSONObject jsonObject = new JSONObject(json);

            for (SkillCategories category : SkillCategories.values()) {
                try {
                    skills.put(category, jsonObject.getDouble(category.toString()));
                } catch (Exception ignored) {
                    skills.put(category, 0.0);
                }
            }

            try {
                skillStatistics = ItemStatistics.fromString(jsonObject.getString("statistics"));
            } catch (Exception e) {
                skillStatistics = ItemStatistics.empty();
                e.printStackTrace();
            }

            return new PlayerSkills(skills, skillStatistics);
        }

        @Override
        public PlayerSkills clone(PlayerSkills value) {
            return new PlayerSkills(value.getSkills(), value.getSkillStatistics());
        }
    };

    public DatapointSkills(String key, DatapointSkills.PlayerSkills value) {
        super(key, value, serializer);
    }

    public DatapointSkills(String key) {
        super(key, PlayerSkills.getDefault(), serializer);
    }

    @Getter
    @AllArgsConstructor
    public static class PlayerSkills {
        private HashMap<SkillCategories, Double> skills;
        private ItemStatistics skillStatistics = ItemStatistics.empty();

        /**
         * Gets the raw experience value for the specified skill category.
         *
         * @param category The skill category to retrieve experience for.
         * @return The raw experience points for the given category.
         */
        public Double getRaw(SkillCategories category) {
            return skills.get(category);
        }

        /**
         * Calculates the cumulative experience in the specified skill category.
         * This excludes the experience required for all previously completed levels.
         *
         * @param category The skill category to retrieve cumulative experience for.
         * @return The cumulative experience value for the given category.
         */
        public Double getCumulative(SkillCategories category) {
            // Minus the requirements of all previous levels
            SkillCategory skillCategory = category.asCategory();
            int level = skillCategory.getLevel(getRaw(category));
            double cumulative = 0.0;

            for (int i = 1; i <= level; i++) {
                cumulative += skillCategory.getReward(i).requirement();
            }

            return getRaw(category) - cumulative;
        }

        /**
         * Sets the player's skill statistics.
         *
         * @param statistics The new skill statistics to set.
         */
        public void setStatistics(ItemStatistics statistics) {
            skillStatistics = statistics;
        }

        /**
         * Updates the raw experience for a given skill category and triggers a SkillUpdateEvent.
         * The event includes details such as the old experience, new experience, and cumulative change.
         *
         * @param player   The player whose skills are being updated.
         * @param category The skill category to update.
         * @param value    The new raw experience value for the given category.
         */
        public void setRaw(SkyBlockPlayer player, SkillCategories category, Double value) {
            HypixelEventHandler.callCustomEvent(new SkillUpdateEvent(
                    player,
                    category,
                    getRaw(category),
                    getCumulative(category),
                    value,
                    getCumulative(category) + value - getRaw(category)
            ));
            skills.put(category, value);
            player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1f, 2f), Sound.Emitter.self());
        }

        /**
         * Increases the experience of a specific skill by the given amount.
         *
         * @param player   The player whose skills are being updated.
         * @param category The skill category to update.
         * @param value    The experience value to add to the existing skill.
         */
        public void increase(SkyBlockPlayer player, SkillCategories category, Double value) {
            setRaw(player, category, getRaw(category) + value);
        }

        /**
         * Creates and returns a default PlayerSkills instance with all skill categories initialized to 0 experience.
         *
         * @return A default PlayerSkills object with zeroed experience in all categories.
         */
        public static PlayerSkills getDefault() {
            HashMap<SkillCategories, Double> skills = new HashMap<>();

            for (SkillCategories category : SkillCategories.values()) {
                skills.put(category, 0.0);
            }

            return new PlayerSkills(skills, ItemStatistics.empty());
        }

        /**
         * Gets the current level of the specified skill category based on the raw experience.
         *
         * @param category The skill category to retrieve the level for.
         * @return The current level for the given skill category.
         */
        public Integer getCurrentLevel(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            return skillCategory.getLevel(getRaw(category));
        }

        /**
         * Gets the next level of the specified skill category.
         * If the current level is the highest, returns null.
         *
         * @param category The skill category to retrieve the next level for.
         * @return The next level, or null if already at the highest level.
         */
        public Integer getNextLevel(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            int level = skillCategory.getLevel(getRaw(category));

            if (level == skillCategory.getHighestLevel()) {
                return null;
            }

            return level + 1;
        }

        /**
         * Calculates the percentage progress towards the next level in the specified skill category.
         * Returns "100" if the player is already at the highest level.
         *
         * @param category The skill category to calculate the percentage for.
         * @return The percentage of progress as a string.
         */
        public String getPercentage(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            int level = skillCategory.getLevel(getRaw(category));
            int nextLevel = level + 1;

            if (nextLevel > skillCategory.getHighestLevel()) {
                return "100";
            }

            double current = getCumulative(category);
            double next = skillCategory.getReward(nextLevel).requirement();

            return String.format("%.2f", (current / next) * 100);
        }

        /**
         * Creates a display list for the player's progress in the specified skill category.
         * It calculates and formats the percentage and visual loading bar to represent the progress.
         *
         * @param lore       The list of strings to which the progress will be added.
         * @param category   The skill category for which to generate the display.
         * @param requirement The requirement to achieve the next level.
         * @param prefix     A prefix string to add to the display.
         * @return A list of strings representing the skill progress.
         */
        public List<String> getDisplay(List<String> lore, SkillCategories category, double requirement, String prefix) {
            double currentHas = getCumulative(category);

            String unlockedPercentage = String.format("%.2f", (currentHas / requirement) * 100);
            lore.add("§7" + prefix + "§e" + unlockedPercentage + "§6%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) Math.round((currentHas / requirement) * maxBarLength);

            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength, // Adjust for added formatting codes
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" +
                    StringUtility.commaify(currentHas) + "§6/§e" + StringUtility.shortenNumber(requirement));

            return lore;
        }
    }
}
