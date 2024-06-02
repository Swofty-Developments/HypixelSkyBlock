package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointQuiver extends Datapoint<DatapointQuiver.PlayerQuiver> {
    public static Serializer<PlayerQuiver> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerQuiver value) {
            Map<String, String> serialized = new HashMap<>();

            for (Map.Entry<Integer, SkyBlockItem> entry : value.getQuiverMap().entrySet()) {
                serialized.put(String.valueOf(entry.getKey()),
                        SkyBlockItemSerializer.serializeJSON(entry.getValue()).toString());
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerQuiver deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Integer, SkyBlockItem> map = new HashMap<>();

            for (String key : obj.keySet()) {
                map.put(Integer.parseInt(key),
                        SkyBlockItemDeserializer.deserializeJSON(new JSONObject(obj.getString(key))));
            }

            return new PlayerQuiver(map);
        }

        @Override
        public PlayerQuiver clone(PlayerQuiver value) {
            return new PlayerQuiver(value.getQuiverMap());
        }
    };

    public DatapointQuiver(String key, PlayerQuiver value) {
        super(key, value, serializer);
    }

    public DatapointQuiver(String key) {
        super(key, new PlayerQuiver(), serializer);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerQuiver {
        private Map<Integer, SkyBlockItem> quiverMap = new HashMap<>();

        public @Nullable SkyBlockItem getFirstItemInQuiver() {
            for (SkyBlockItem item : quiverMap.values()) {
                if (item == null) continue;
                if (item.getAmount() == 0) continue;
                if (item.isNA()) continue;
                if (item.getMaterial() == Material.AIR) continue;

                return item;
            }
            return null;
        }

        public @Nullable SkyBlockItem getInSlot(int slot) {
            return quiverMap.get(slot);
        }

        public void setFirstItemInQuiver(SkyBlockItem item) {
            int firstFilledSlot = -1;
            for (int i = 0; i < 9; i++) {
                if (quiverMap.get(i) != null) {
                    firstFilledSlot = i;
                    break;
                }
            }

            if (firstFilledSlot == -1) {
                quiverMap.put(0, item);
                return;
            }

            quiverMap.put(firstFilledSlot, item);
        }

        public boolean isEmpty() {
            return getFirstItemInQuiver() == null;
        }

        public Integer getAmountOfArrows(ItemType arrowType) {
            int amount = 0;
            for (SkyBlockItem item : quiverMap.values()) {
                if (item == null) continue;
                if (item.getAmount() == 0) continue;
                if (item.isNA()) continue;
                if (item.getMaterial() == Material.AIR) continue;

                if (item.getAttributeHandler().getItemTypeAsType() == arrowType) {
                    amount += item.getAmount();
                }
            }
            return amount;
        }
    }
}
