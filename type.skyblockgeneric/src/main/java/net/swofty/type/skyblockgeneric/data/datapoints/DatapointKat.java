package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointKat extends SkyBlockDatapoint<DatapointKat.PlayerKat> {
    public static Serializer<PlayerKat> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerKat value) {
            Map<String, String> serialized = new HashMap<>();

            for (Map.Entry<Long, SkyBlockItem> entry : value.katMap().entrySet()) {
                serialized.put(String.valueOf(entry.getKey()), entry.getValue().toUnderstandable().serialize());
            }
            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerKat deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Long, SkyBlockItem> katMap = new HashMap<>();

            for (String key : obj.keySet()) {
                SkyBlockItem item = new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(obj.getString(key)));
                katMap.put(Long.parseLong(key), item);
            }
            return new PlayerKat(katMap);
        }

        @Override
        public PlayerKat clone(PlayerKat value) {
            return new PlayerKat(new HashMap<>(value.katMap()));
        }
    };

    public DatapointKat(String key, PlayerKat value) {
        super(key, value, serializer);
    }

    public DatapointKat(String key) {
        super(key, new PlayerKat(new HashMap<>()), serializer);
    }

    public record PlayerKat(Map<Long, SkyBlockItem> katMap) {

        public long getFinishTime() {
            for (long timestamp : katMap.keySet()) {
                return timestamp;
            }
            return 0;
        }

        @Nullable
        public SkyBlockItem getPet() {
            for (SkyBlockItem pet : katMap.values()) {
                return pet;
            }
            return null;
        }

        public void setKatMap(Long timestamp, SkyBlockItem item) {
            katMap.put(timestamp, item);
        }

        public void setFinishedTime(Long timestamp) {
            setKatMap(timestamp, getPet());
        }
    }
}
