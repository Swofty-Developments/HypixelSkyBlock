package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
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
                serialized.put(
                        String.valueOf(entry.getKey()),
                        entry.getValue().toUnderstandable().serialize()
                ).toString();
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerQuiver deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Integer, SkyBlockItem> map = new HashMap<>();

            for (String key : obj.keySet()) {
                map.put(Integer.parseInt(key),
                        new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(obj.getString(key))));
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

        /**
         * Retrieves the first valid arrow item in the quiver.
         *
         * @return the first valid {@link SkyBlockItem} in the quiver, or null if no valid items are found
         */
        public @Nullable SkyBlockItem getFirstItemInQuiver() {
            for (SkyBlockItem item : quiverMap.values()) {
                if (item == null || item.getAmount() == 0 || item.isNA() || item.getMaterial() == Material.AIR) {
                    continue;
                }
                return item;
            }
            return null;
        }

        /**
         * Retrieves the item in a specific slot of the quiver.
         *
         * @param slot the slot index to retrieve the item from (0-8)
         * @return the {@link SkyBlockItem} in the specified slot, or null if the slot is empty
         */
        public @Nullable SkyBlockItem getInSlot(int slot) {
            return quiverMap.get(slot);
        }

        /**
         * Sets the first item in the quiver to the provided item.
         * If the quiver is empty, it places the item in the first slot; otherwise, it replaces the
         * first filled slot with the new item.
         *
         * @param item the {@link SkyBlockItem} to set as the first item in the quiver
         */
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
            } else {
                quiverMap.put(firstFilledSlot, item);
            }
        }

        /**
         * Checks if the quiver is empty (i.e., contains no valid arrow items).
         *
         * @return true if the quiver is empty, false otherwise
         */
        public boolean isEmpty() {
            return getFirstItemInQuiver() == null;
        }

        /**
         * Calculates the total amount of arrows of a specific type in the quiver.
         *
         * @param arrowType the {@link ItemTypeLinker} representing the type of arrows to count
         * @return the total amount of arrows of the specified type in the quiver
         */
        public Integer getAmountOfArrows(ItemTypeLinker arrowType) {
            int amount = 0;
            for (SkyBlockItem item : quiverMap.values()) {
                if (item == null || item.getAmount() == 0 || item.isNA() || item.getMaterial() == Material.AIR) {
                    continue;
                }

                if (item.getAttributeHandler().getPotentialClassLinker() == arrowType) {
                    amount += item.getAmount();
                }
            }
            return amount;
        }
    }
}
