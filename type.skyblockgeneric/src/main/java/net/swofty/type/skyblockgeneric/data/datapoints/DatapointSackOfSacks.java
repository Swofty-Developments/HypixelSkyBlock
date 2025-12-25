package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.*;

public class DatapointSackOfSacks extends SkyBlockDatapoint<DatapointSackOfSacks.PlayerSackOfSacks> {
    public static Serializer<PlayerSackOfSacks> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerSackOfSacks value) {
            Map<String, String> serialized = new HashMap<>();

            for (Map.Entry<Integer, SkyBlockItem> entry : value.sackMap()) {
                serialized.put(String.valueOf(entry.getKey()), entry.getValue().toUnderstandable().serialize());
            }
            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerSackOfSacks deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            List<Map.Entry<Integer, SkyBlockItem>> sackMap = new ArrayList<>();

            for (String key : obj.keySet()) {
                SkyBlockItem item = new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(obj.getString(key)));
                sackMap.add(Map.entry(Integer.parseInt(key), item));

            }
            return new PlayerSackOfSacks(sackMap);
        }

        @Override
        public PlayerSackOfSacks clone(PlayerSackOfSacks value) {
            return new PlayerSackOfSacks(new ArrayList<>(value.sackMap));
        }
    };

    public DatapointSackOfSacks(String key, PlayerSackOfSacks value) {
        super(key, value, serializer);
    }

    public DatapointSackOfSacks(String key) {
        super(key, new PlayerSackOfSacks(new ArrayList<>()), serializer);
    }

    public record PlayerSackOfSacks(List<Map.Entry<Integer, SkyBlockItem>> sackMap) {

        /**
         * Retrieves the sack item located in the specified slot.
         *
         * @param slot the slot index of the sack
         * @return the {@link SkyBlockItem} in the specified slot, or {@code null} if no item exists
         */
        public @Nullable SkyBlockItem getInSlot(int slot) {
            for (Map.Entry<Integer, SkyBlockItem> itemEntry : sackMap) {
                if (itemEntry.getKey() == slot) {
                    return itemEntry.getValue();
                }
            }
            return null;
        }

        /**
         * Retrieves a list of all sack items currently in the sack map.
         *
         * @return an unmodifiable list of {@link SkyBlockItem} representing all sacks
         */
        public List<SkyBlockItem> getAllSacks() {
            List<SkyBlockItem> items = new ArrayList<>();
            for (Map.Entry<Integer, SkyBlockItem> itemEntry : List.copyOf(sackMap)) {
                items.add(itemEntry.getValue());
            }
            return items;
        }

        /**
         * Removes the sack item from the specified slot.
         *
         * @param slot the slot index of the sack to remove
         */
        public void removeFromSlot(int slot) {
            sackMap.removeIf(itemEntry -> itemEntry.getKey() == slot);
        }

        /**
         * Sets the specified sack item in the given slot.
         * If the slot is already taken, the existing item is replaced.
         *
         * @param slot the slot index where the sack should be placed
         * @param item the {@link SkyBlockItem} to place in the specified slot
         */
        public void setInSlot(int slot, SkyBlockItem item) {
            boolean slotTaken = false;
            for (int i = 0; i < sackMap.size(); i++) {
                if (sackMap.get(i).getKey() == slot) {
                    // Update the item in the existing slot
                    AbstractMap.SimpleEntry<Integer, SkyBlockItem> map = new AbstractMap.SimpleEntry<>(slot, item);
                    sackMap.remove(sackMap.get(i));
                    sackMap.add(map);
                    slotTaken = true;
                }
            }
            // Add new item if the slot was not already taken
            if (!slotTaken) {
                sackMap.add(Map.entry(slot, item));
            }
        }
    }
}
