package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

/**
 * Holds persistent experimentation-related state, such as Superpairs bonus clicks.
 */
public class DatapointExperimentation extends SkyBlockDatapoint<DatapointExperimentation.PlayerExperimentation> {
    private static final Serializer<PlayerExperimentation> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerExperimentation value) {
            JSONObject json = new JSONObject();
            json.put("superpairs_bonus_clicks", value.superpairsBonusClicks);
            return json.toString();
        }

        @Override
        public PlayerExperimentation deserialize(String json) {
            if (json == null || json.isEmpty()) return new PlayerExperimentation();
            JSONObject obj = new JSONObject(json);
            PlayerExperimentation pe = new PlayerExperimentation();
            pe.superpairsBonusClicks = obj.optInt("superpairs_bonus_clicks", 0);
            return pe;
        }

        @Override
        public PlayerExperimentation clone(PlayerExperimentation value) {
            return new PlayerExperimentation(value.superpairsBonusClicks);
        }
    };

    public DatapointExperimentation(String key, PlayerExperimentation value) {
        super(key, value, serializer);
    }

    public DatapointExperimentation(String key) {
        super(key, new PlayerExperimentation(), serializer);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerExperimentation {
        public int superpairsBonusClicks = 0;
    }
}


