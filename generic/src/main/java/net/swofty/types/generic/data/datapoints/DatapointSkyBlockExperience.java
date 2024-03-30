package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointSkyBlockExperience extends Datapoint<DatapointSkyBlockExperience.PlayerSkyBlockExperience> {
    private static final Serializer<PlayerSkyBlockExperience> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerSkyBlockExperience value) {
            List<String> serialized = new ArrayList<>();

            value.getCompletedExperienceCauses().forEach((cause) -> {
                serialized.add(SkyBlockLevelCause.getKey(cause));
            });

            return new JSONObject(new HashMap<>(Map.of("values", serialized))).toString();
        }

        @Override
        public PlayerSkyBlockExperience deserialize(String json) {
            List<SkyBlockLevelCauseAbstr> experience = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(json);
            jsonObject.getJSONArray("values").forEach((value) -> {
                experience.add(SkyBlockLevelCause.getCause((String) value));
            });

            return new PlayerSkyBlockExperience(experience);
        }

        @Override
        public PlayerSkyBlockExperience clone(PlayerSkyBlockExperience value) {
            return new PlayerSkyBlockExperience(value.getCompletedExperienceCauses());
        }
    };

    public DatapointSkyBlockExperience(String key, PlayerSkyBlockExperience value) {
        super(key, value, serializer);
    }

    public DatapointSkyBlockExperience(String key) {
        this(key, new PlayerSkyBlockExperience());
    }

    @NoArgsConstructor
    @Getter
    public static class PlayerSkyBlockExperience {
        private List<SkyBlockLevelCauseAbstr> completedExperienceCauses = new ArrayList<>();

        public PlayerSkyBlockExperience(List<SkyBlockLevelCauseAbstr> completedExperienceCauses) {
            this.completedExperienceCauses = completedExperienceCauses;
        }

        public boolean hasExperienceFor(SkyBlockLevelCauseAbstr cause) {
            return completedExperienceCauses.contains(cause);
        }

        public void addExperience(SkyBlockLevelCauseAbstr cause) {
            if (completedExperienceCauses.contains(cause)) return;
            completedExperienceCauses.add(cause);
        }

        public String getNextLevelDisplay() {
            SkyBlockLevelRequirement nextLevel = getLevel().getNextLevel();
            if (nextLevel == null) return "§cMAX";

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((getTotalXP() / (double) nextLevel.getExperience()) * maxBarLength);

            String completedLoadingBar = "§3§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§f§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            return "§7" + completedLoadingBar + uncompletedLoadingBar + "§r §b" + getTotalXP() + "§3/§b" + nextLevel.getExperience();
        }

        public Double getTotalXP() {
            if (completedExperienceCauses.isEmpty()) return 0.0;
            return completedExperienceCauses.stream().mapToDouble(SkyBlockLevelCauseAbstr::xpReward).sum();
        }

        public SkyBlockLevelRequirement getLevel() {
            return SkyBlockLevelRequirement.getFromXP(getTotalXP());
        }
    }
}
