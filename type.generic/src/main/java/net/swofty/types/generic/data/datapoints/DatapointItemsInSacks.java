package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemTypeLinker;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointItemsInSacks extends Datapoint<DatapointItemsInSacks.PlayerItemsInSacks> {

    public DatapointItemsInSacks(String key, PlayerItemsInSacks value) {
        super(key, value, new Serializer<>() {
            public String serialize(PlayerItemsInSacks value) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                for (Map.Entry<ItemTypeLinker, Integer> entry : value.items.entrySet()) {
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
                Map<ItemTypeLinker, Integer> sacks = new HashMap<>();
                for (String key : jsonObject.keySet()) {
                    sacks.put(ItemTypeLinker.valueOf(key), jsonObject.getInt(key));
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
        private Map<ItemTypeLinker, Integer> items;

        /**
         * Sets the amount of a specific item type.
         *
         * @param type   the {@link ItemTypeLinker} representing the item type
         * @param amount the amount to set for the specified item type
         */
        public void set(ItemTypeLinker type, int amount) {
            items.put(type, amount);
        }

        /**
         * Increases the amount of a specific item type by a given amount.
         *
         * @param type   the {@link ItemTypeLinker} representing the item type
         * @param amount the amount to increase for the specified item type
         */
        public void increase(ItemTypeLinker type, Integer amount) {
            items.put(type, getAmount(type) + amount);
        }

        /**
         * Decreases the amount of a specific item type by a given amount.
         *
         * @param type   the {@link ItemTypeLinker} representing the item type
         * @param amount the amount to decrease for the specified item type
         */
        public void decrease(ItemTypeLinker type, Integer amount) {
            items.put(type, getAmount(type) - amount);
        }

        /**
         * Retrieves the amount of a specific item type.
         *
         * @param item the {@link ItemTypeLinker} representing the item type
         * @return the amount of the specified item type, or 0 if it does not exist
         */
        public Integer getAmount(ItemTypeLinker item) {
            return items.getOrDefault(item, 0);
        }
    }
}
