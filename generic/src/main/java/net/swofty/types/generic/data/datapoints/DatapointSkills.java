package net.swofty.types.generic.data.datapoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.serializer.Serializer;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.utility.StringUtility;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointSkills extends Datapoint<DatapointSkills.PlayerSkills> {
    private static final Serializer<PlayerSkills> serializer = new Serializer<>() {

        @Override
        public String serialize(PlayerSkills value) {
            JSONObject jsonObject = new JSONObject();

            for (SkillCategories category : SkillCategories.values()) {
                jsonObject.put(category.toString(), value.get(category));
            }

            for (ItemStatistic statistic : ItemStatistic.values()) {
                jsonObject.put(statistic.toString(), value.getStatistic(statistic));
            }

            return jsonObject.toString();
        }

        @Override
        public PlayerSkills deserialize(String json) {
            HashMap<SkillCategories, Double> skills = new HashMap<>(PlayerSkills.getDefault().getSkills());
            HashMap<ItemStatistic, Double> skillStatistics = new HashMap<>(PlayerSkills.getDefault().getSkillStatistics());

            if (json == null || json.isEmpty()) {
                return new PlayerSkills(skills, skillStatistics);
            }

            JSONObject jsonObject = new JSONObject(json);

            for (SkillCategories category : SkillCategories.values()) {
                skills.put(category, jsonObject.getDouble(category.toString()));
            }

            for (ItemStatistic statistic : ItemStatistic.values()) {
                skillStatistics.put(statistic, jsonObject.getDouble(statistic.toString()));
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
        private Map<SkillCategories, Double> skills;
        private Map<ItemStatistic, Double> skillStatistics;

        public Double get(SkillCategories category) {
            return skills.get(category);
        }

        public void addStatistic(ItemStatistic statistic, double amount) {
            if (skillStatistics.containsKey(statistic)) {
                skillStatistics.put(statistic, skillStatistics.get(statistic) + amount);
            } else {
                skillStatistics.put(statistic, amount);
            }
        }

        public Double getStatistic(ItemStatistic statistic) {
            if (!skillStatistics.containsKey(statistic))
                return 0.0;
            return skillStatistics.get(statistic);
        }

        public void set(SkyBlockPlayer player, SkillCategories category, Double value) {
            SkyBlockEvent.callSkyBlockEvent(new SkillUpdateEvent(
                    player,
                    category,
                    get(category),
                    value
            ));
            skills.put(category, value);
        }

        public static PlayerSkills getDefault() {
            return new PlayerSkills(Map.of(
                SkillCategories.COMBAT, 0.0,
                SkillCategories.FARMING, 0.0,
                SkillCategories.FISHING, 0.0,
                SkillCategories.MINING, 0.0,
                SkillCategories.FORAGING, 0.0,
                SkillCategories.ENCHANTING, 0.0
            ), new HashMap<>());
        }

        public Integer getCurrentLevel(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            return skillCategory.getLevel(get(category));
        }

        public Integer getNextLevel(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            int level = skillCategory.getLevel(get(category));

            if (level == skillCategory.getHighestLevel()) {
                return null;
            }

            return level + 1;
        }

        public String getPercentage(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            int level = skillCategory.getLevel(get(category));
            int nextLevel = level + 1;

            if (nextLevel > skillCategory.getHighestLevel()) {
                return "100";
            }

            double current = get(category);
            double next = skillCategory.getReward(nextLevel).requirement();

            return String.format("%.2f", (current / next) * 100);
        }

        public List<String> getDisplay(List<String> lore, SkillCategories category, double requirement, String prefix) {
            double currentHas = get(category);

            String unlockedPercentage = String.format("%.2f", (currentHas / requirement) * 100);
            lore.add("§7" + prefix + "§e" + unlockedPercentage + "§6%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) Math.round((currentHas / requirement) * maxBarLength);

            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + StringUtility.commaify(currentHas) +
                    "§6/§e" + StringUtility.commaify(requirement));

            return lore;
        }
    }
}
