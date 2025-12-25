package net.swofty.type.skyblockgeneric.data.datapoints;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointBackpacks extends SkyBlockDatapoint<DatapointBackpacks.PlayerBackpacks> {
    private static final Serializer<PlayerBackpacks> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerBackpacks value) {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<Integer, UnderstandableSkyBlockItem> entry : value.backpacks.entrySet()) {
                jsonObject.put(entry.getKey().toString(), entry.getValue().serialize());
            }
            jsonObject.put("unlockedSlots", value.unlockedSlots);
            return jsonObject.toString();
        }

        @Override
        public PlayerBackpacks deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);
            Map<Integer, UnderstandableSkyBlockItem> backpacks = new HashMap<>();
            for (Object key : jsonObject.keySet()) {
                if (key.equals("unlockedSlots")) continue;
                backpacks.put(Integer.parseInt(key.toString()), UnderstandableSkyBlockItem.deserialize(jsonObject.getString(key.toString())));
            }
            int unlockedSlots = jsonObject.getInt("unlockedSlots");
            return new PlayerBackpacks(backpacks, unlockedSlots);
        }

        @Override
        public PlayerBackpacks clone(PlayerBackpacks value) {
            return new PlayerBackpacks(value.backpacks, value.unlockedSlots);
        }
    };

    public DatapointBackpacks(String key, DatapointBackpacks.PlayerBackpacks value) {
        super(key, value, serializer);
    }

    public DatapointBackpacks(String key) {
        super(key, new PlayerBackpacks(), serializer);
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlayerBackpacks {
        private final Map<Integer, UnderstandableSkyBlockItem> backpacks;
        @Setter
        private int unlockedSlots;

        public PlayerBackpacks() {
            this.backpacks = new HashMap<>();
            this.unlockedSlots = 1;
        }

        public PlayerBackpacks(Map<Integer, UnderstandableSkyBlockItem> backpacks, int unlockedSlots) {
            this.backpacks = backpacks;
            this.unlockedSlots = unlockedSlots;
        }

        public int getHighestBackpackSlot() {
            return backpacks.keySet().stream().max(Integer::compareTo).orElse(0);
        }

        public int getLowestBackpackSlot() {
            return backpacks.keySet().stream().min(Integer::compareTo).orElse(0);
        }
    }
}