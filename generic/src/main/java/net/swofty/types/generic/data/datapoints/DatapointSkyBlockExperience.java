package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointSkyBlockExperience extends Datapoint<DatapointSkyBlockExperience.PlayerSkyBlockExperience> {
    private static final Serializer<PlayerSkyBlockExperience> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerSkyBlockExperience value) {
            Map<String, String> serialized = new HashMap<>();

            value.getExperience().forEach((cause, exp) -> {
                serialized.put(SkyBlockLevelCause.getKey(cause), String.valueOf(exp));
            });

            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerSkyBlockExperience deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<SkyBlockLevelCauseAbstr, Double> experience = new HashMap<>();

            for (String key : obj.keySet()) {
                experience.put(SkyBlockLevelCause.getCause(key), obj.getDouble(key));
            }

            return new PlayerSkyBlockExperience(experience);
        }

        @Override
        public PlayerSkyBlockExperience clone(PlayerSkyBlockExperience value) {
            return new PlayerSkyBlockExperience(value.getExperience());
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
        private Map<SkyBlockLevelCauseAbstr, Double> experience = new HashMap<>();

        public PlayerSkyBlockExperience(Map<SkyBlockLevelCauseAbstr, Double> experience) {
            this.experience = experience;
        }

        public boolean hasExperienceFor(SkyBlockLevelCauseAbstr cause) {
            return experience.containsKey(cause);
        }

        public void addExperience(SkyBlockLevelCauseAbstr cause, double amount) {
            experience.put(cause, experience.getOrDefault(cause, 0.0) + amount);
        }

        public Double getTotalXP() {
            return experience.values().stream().mapToDouble(Double::doubleValue).sum();
        }

        public SkyBlockLevelRequirement getLevel() {
            return SkyBlockLevelRequirement.getFromXP(getTotalXP());
        }
    }
}
