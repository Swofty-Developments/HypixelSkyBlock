package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointAccessoryBag extends Datapoint<DatapointAccessoryBag.PlayerAccessoryBag> {
    public static Serializer<DatapointAccessoryBag.PlayerAccessoryBag> serializer = new Serializer<>() {
        @Override
        public String serialize(DatapointAccessoryBag.PlayerAccessoryBag value) {
            Map<String, String> serialized = new HashMap<>();

            serialized.put("discoveredAccessories", new JSONObject(value.getDiscoveredAccessories()).toString());

            for (Map.Entry<Integer, SkyBlockItem> entry : value.getAccessoryMap().entrySet()) {
                serialized.put(String.valueOf(entry.getKey()),
                        SkyBlockItemSerializer.serializeJSON(entry.getValue()).toString());
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public DatapointAccessoryBag.PlayerAccessoryBag deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Integer, SkyBlockItem> map = new HashMap<>();
            List<ItemType> discoveredAccessories = new ArrayList<>();

            for (String key : obj.keySet()) {
                if (key.equals("discoveredAccessories")) {
                    if (obj.isNull(key)) continue;
                    if (obj.get(key) instanceof String) continue;
                    if (obj.getJSONArray(key).isEmpty()) continue;
                    for (Object o : obj.getJSONArray(key)) {
                        discoveredAccessories.add(ItemType.valueOf(o.toString()));
                    }
                    continue;
                }

                map.put(Integer.parseInt(key),
                        SkyBlockItemDeserializer.deserializeJSON(new JSONObject(obj.getString(key))));
            }

            return new DatapointAccessoryBag.PlayerAccessoryBag(map, discoveredAccessories);
        }

        @Override
        public DatapointAccessoryBag.PlayerAccessoryBag clone(DatapointAccessoryBag.PlayerAccessoryBag value) {
            return new DatapointAccessoryBag.PlayerAccessoryBag(value.getAccessoryMap(), value.getDiscoveredAccessories());
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
        private List<ItemType> discoveredAccessories = new ArrayList<>();

        public @Nullable SkyBlockItem getInSlot(int slot) {
            return accessoryMap.get(slot);
        }

        public List<SkyBlockItem> getAllAccessories() {
            return List.copyOf(accessoryMap.values());
        }

        public void addDiscoveredAccessory(ItemType type) {
            if (discoveredAccessories.contains(type)) return;
            discoveredAccessories.add(type);
        }

        public boolean hasDiscoveredAccessory(ItemType type) {
            return discoveredAccessories.contains(type);
        }

        public void removeFromSlot(int slot) {
            accessoryMap.remove(slot);
        }

        public void setInSlot(int slot, SkyBlockItem item) {
            accessoryMap.put(slot, item);
        }
    }
}
