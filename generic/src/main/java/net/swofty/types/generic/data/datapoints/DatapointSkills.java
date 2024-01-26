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
import net.swofty.types.generic.utility.StringUtility;

import java.util.List;
import java.util.Map;

public class DatapointSkills extends Datapoint<DatapointSkills.PlayerSkills> {
    private static Serializer<PlayerSkills> serializer = new Serializer<>() {

        @Override
        public String serialize(PlayerSkills value) {
            String toReturn = "";

            for (Map.Entry<SkillCategories, Double> entry : value.getSkills().entrySet()) {
                toReturn += entry.getKey().name() + ":" + entry.getValue() + ",";
            }

            return toReturn;
        }

        @Override
        public PlayerSkills deserialize(String json) {
            Map<SkillCategories, Double> skills = PlayerSkills.getDefault().getSkills();

            for (String entry : json.split(",")) {
                String[] split = entry.split(":");
                skills.put(SkillCategories.valueOf(split[0]), Double.parseDouble(split[1]));
            }

            return new PlayerSkills(skills);
        }

        @Override
        public PlayerSkills clone(PlayerSkills value) {
            return new PlayerSkills(value.getSkills());
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

        public Double get(SkillCategories category) {
            return skills.get(category);
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
            ));
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
