package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointAccessoryBag extends Datapoint<DatapointAccessoryBag.PlayerAccessoryBag> {
    public static Serializer<DatapointAccessoryBag.PlayerAccessoryBag> serializer = new Serializer<>() {
        @Override
        public String serialize(DatapointAccessoryBag.PlayerAccessoryBag value) {
            Map<Integer, String> serialized = new HashMap<>();

            for (Map.Entry<Integer, SkyBlockItem> entry : value.getAccessoryMap().entrySet()) {
                serialized.put(entry.getKey(),
                        SkyBlockItemSerializer.serializeJSON(entry.getValue()).toString());
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public DatapointAccessoryBag.PlayerAccessoryBag deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Integer, SkyBlockItem> map = new HashMap<>();

            for (String key : obj.keySet()) {
                map.put(Integer.parseInt(key),
                        SkyBlockItemDeserializer.deserializeJSON(obj.getJSONObject(key)));
            }

            return new DatapointAccessoryBag.PlayerAccessoryBag(map);
        }

        @Override
        public DatapointAccessoryBag.PlayerAccessoryBag clone(DatapointAccessoryBag.PlayerAccessoryBag value) {
            return new DatapointAccessoryBag.PlayerAccessoryBag(value.getAccessoryMap());
        }
    };

    public DatapointAccessoryBag(String key, DatapointAccessoryBag.PlayerAccessoryBag value) {
        super(key, value, serializer);
    }

    public DatapointAccessoryBag(String key) {
        super(key, new DatapointAccessoryBag.PlayerAccessoryBag(), serializer);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerAccessoryBag {
        private Map<Integer, SkyBlockItem> accessoryMap = new HashMap<>();

        public @Nullable SkyBlockItem getInSlot(int slot) {
            return accessoryMap.get(slot);
        }

        public List<SkyBlockItem> getAllAccessories() {
            return List.copyOf(accessoryMap.values());
        }

        public void removeFromSlot(int slot) {
            accessoryMap.remove(slot);
        }

        public void setInSlot(int slot, SkyBlockItem item) {
            accessoryMap.put(slot, item);
        }
    }
}
