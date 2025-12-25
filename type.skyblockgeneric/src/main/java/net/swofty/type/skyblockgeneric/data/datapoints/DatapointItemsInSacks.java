package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointItemsInSacks extends SkyBlockDatapoint<DatapointItemsInSacks.PlayerItemsInSacks> {

    public DatapointItemsInSacks(String key, PlayerItemsInSacks value) {
        super(key, value, new Serializer<>() {
            public String serialize(PlayerItemsInSacks value) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                for (Map.Entry<ItemType, Integer> entry : value.items.entrySet()) {
                    sb.append("\"").append(entry.getKey().toString()).append("\":").append(entry.getValue()).append(",");
                }
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("}");
                return sb.toString();
            }

            @Override
            public PlayerItemsInSacks deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                Map<ItemType, Integer> sacks = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    sacks.put(ItemType.valueOf(key), jsonObject.getInt(key));
                }
                return new PlayerItemsInSacks(sacks);
            }

            @Override
            public PlayerItemsInSacks clone(PlayerItemsInSacks value) {
                return new PlayerItemsInSacks(new HashMap<>(value.items));
            }
        });
    }

    public DatapointItemsInSacks(String key) {
        this(key, new PlayerItemsInSacks(new HashMap<>()));
    }

    @Getter
    @AllArgsConstructor
    public static class PlayerItemsInSacks {
        private Map<ItemType, Integer> items;

        public void set(ItemType type, int amount) {
            items.put(type, amount);
        }

        public void increase(ItemType type, Integer amount) {
            items.put(type, getAmount(type) + amount);
        }

        public void decrease(ItemType type, Integer amount) {
            items.put(type, getAmount(type) - amount);
        }

        public Integer getAmount(ItemType item) {
            return items.getOrDefault(item, 0);
        }
    }
}
