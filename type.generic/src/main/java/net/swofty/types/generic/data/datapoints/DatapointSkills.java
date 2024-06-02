package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
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
            } catch (Exception ignored) {
                skillStatistics = ItemStatistics.empty();
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
        private ItemStatistics skillStatistics = ItemStatistics.empty();

        public Double getRaw(SkillCategories category) {
            return skills.get(category);
        }

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

        public void setStatistics(ItemStatistics statistics) {
            skillStatistics = statistics;
        }

        public void setRaw(SkyBlockPlayer player, SkillCategories category, Double value) {
            SkyBlockEventHandler.callSkyBlockEvent(new SkillUpdateEvent(
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

        public static PlayerSkills getDefault() {
            return new PlayerSkills(Map.of(
                SkillCategories.COMBAT, 0.0,
                SkillCategories.FARMING, 0.0,
                SkillCategories.FISHING, 0.0,
                SkillCategories.MINING, 0.0,
                SkillCategories.FORAGING, 0.0,
                SkillCategories.ENCHANTING, 0.0
            ), ItemStatistics.empty());
        }

        public Integer getCurrentLevel(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            return skillCategory.getLevel(getRaw(category));
        }

        public Integer getNextLevel(SkillCategories category) {
            SkillCategory skillCategory = category.asCategory();

            int level = skillCategory.getLevel(getRaw(category));

            if (level == skillCategory.getHighestLevel()) {
                return null;
            }

            return level + 1;
        }

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
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" +
                    StringUtility.commaify(currentHas) + "§6/§e" + StringUtility.shortenNumber(requirement));

            return lore;
        }
    }
}
