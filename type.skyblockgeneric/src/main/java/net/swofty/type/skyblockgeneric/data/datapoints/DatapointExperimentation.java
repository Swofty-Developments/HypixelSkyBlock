package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

public final class DatapointExperimentation extends SkyBlockDatapoint<DatapointExperimentation.PlayerExperimentation> {
    private static final Serializer<PlayerExperimentation> SERIALIZER = new Serializer<>() {
        @Override
        public String serialize(PlayerExperimentation value) {
            return new JSONObject().put("superpairs_bonus_clicks", value.superpairsBonusClicks()).toString();
        }

        @Override
        public PlayerExperimentation deserialize(String json) {
            if (json == null || json.isBlank()) return new PlayerExperimentation(0);
            return new PlayerExperimentation(new JSONObject(json).optInt("superpairs_bonus_clicks", 0));
        }

        @Override
        public PlayerExperimentation clone(PlayerExperimentation value) {
            return new PlayerExperimentation(value.superpairsBonusClicks());
        }
    };

    public DatapointExperimentation(String key) {
        super(key, new PlayerExperimentation(0), SERIALIZER);
    }

    public record PlayerExperimentation(int superpairsBonusClicks) {
        public PlayerExperimentation {
            if (superpairsBonusClicks < 0) throw new IllegalArgumentException("Bonus clicks cannot be negative");
        }
    }
}
