package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatapointCollectedMobTypeRewards extends SkyBlockDatapoint<DatapointCollectedMobTypeRewards.PlayerCollectedMobTypeRewards> {

    public DatapointCollectedMobTypeRewards(String key, PlayerCollectedMobTypeRewards value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(PlayerCollectedMobTypeRewards value) {
                return new JSONArray(value.collectedMobTypes).toString();
            }

            @Override
            public PlayerCollectedMobTypeRewards deserialize(String json) {
                JSONArray array = new JSONArray(json);
                List<String> collectedMobTypes = new ArrayList<>();

                array.forEach(value -> {
                    if (value instanceof String)
                        collectedMobTypes.add((String) value);
                });

                return new PlayerCollectedMobTypeRewards(collectedMobTypes);
            }

            @Override
            public PlayerCollectedMobTypeRewards clone(PlayerCollectedMobTypeRewards value) {
                return new PlayerCollectedMobTypeRewards(value.collectedMobTypes == null ? new ArrayList<>() : new ArrayList<>(value.collectedMobTypes));
            }
        });
    }

    public DatapointCollectedMobTypeRewards(String key) {
        this(key, new PlayerCollectedMobTypeRewards());
    }

    @NoArgsConstructor
    @Getter
    public static class PlayerCollectedMobTypeRewards {
        private List<String> collectedMobTypes = new ArrayList<>();

        public PlayerCollectedMobTypeRewards(List<String> collectedMobTypes) {
            this.collectedMobTypes = collectedMobTypes;
        }

        public boolean hasClaimed(String mobType) {
            return collectedMobTypes.contains(mobType);
        }

        public void claim(String mobType) {
            if (collectedMobTypes.contains(mobType)) return;
            collectedMobTypes.add(mobType);
        }
    }
}
