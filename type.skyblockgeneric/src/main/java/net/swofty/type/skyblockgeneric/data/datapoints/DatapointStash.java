package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointStash extends SkyBlockDatapoint<DatapointStash.PlayerStash> {
    public static final int ITEM_STASH_LIMIT = 720;
    public static final int ITEM_STASH_WARNING_THRESHOLD = 648; // 90% of 720

    private static final Serializer<PlayerStash> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerStash value) {
            JSONObject jsonObject = new JSONObject();

            // Serialize item stash (non-stackable items)
            JSONArray itemStashArray = new JSONArray();
            for (SkyBlockItem item : value.itemStash) {
                if (item == null) {
                    itemStashArray.put("null");
                } else {
                    itemStashArray.put(item.toUnderstandable().serialize());
                }
            }
            jsonObject.put("itemStash", itemStashArray);

            // Serialize material stash (stackable items as type -> count)
            JSONObject materialStashObj = new JSONObject();
            for (Map.Entry<ItemType, Integer> entry : value.materialStash.entrySet()) {
                materialStashObj.put(entry.getKey().toString(), entry.getValue());
            }
            jsonObject.put("materialStash", materialStashObj);

            return jsonObject.toString();
        }

        @Override
        public PlayerStash deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);

            // Deserialize item stash
            List<SkyBlockItem> itemStash = new ArrayList<>();
            if (jsonObject.has("itemStash")) {
                JSONArray itemStashArray = jsonObject.getJSONArray("itemStash");
                for (int i = 0; i < itemStashArray.length(); i++) {
                    Object item = itemStashArray.get(i);
                    if (item.equals("null")) {
                        // Skip null items in stash
                        continue;
                    }
                    itemStash.add(new SkyBlockItem(new UnderstandableSkyBlockItemSerializer().deserialize((String) item)));
                }
            }

            // Deserialize material stash
            Map<ItemType, Integer> materialStash = new HashMap<>();
            if (jsonObject.has("materialStash")) {
                JSONObject materialStashObj = jsonObject.getJSONObject("materialStash");
                for (String key : materialStashObj.keySet()) {
                    materialStash.put(ItemType.valueOf(key), materialStashObj.getInt(key));
                }
            }

            return new PlayerStash(itemStash, materialStash);
        }

        @Override
        public PlayerStash clone(PlayerStash value) {
            return new PlayerStash(
                    new ArrayList<>(value.itemStash),
                    new HashMap<>(value.materialStash)
            );
        }
    };

    public DatapointStash(String key, PlayerStash value) {
        super(key, value, serializer);
    }

    public DatapointStash(String key) {
        this(key, new PlayerStash());
    }

    @Getter
    @NoArgsConstructor
    public static class PlayerStash {
        private List<SkyBlockItem> itemStash = new ArrayList<>();
        private Map<ItemType, Integer> materialStash = new HashMap<>();

        public PlayerStash(List<SkyBlockItem> itemStash, Map<ItemType, Integer> materialStash) {
            this.itemStash = new ArrayList<>(itemStash);
            this.materialStash = new HashMap<>(materialStash);
        }

        /**
         * Add a non-stackable item to the item stash.
         * @param item The item to add
         * @return true if added successfully, false if stash is at limit (720)
         */
        public boolean addToItemStash(SkyBlockItem item) {
            if (itemStash.size() >= ITEM_STASH_LIMIT) {
                return false;
            }
            itemStash.add(item);
            return true;
        }

        /**
         * Add stackable materials to the material stash.
         * @param type The item type
         * @param amount The amount to add
         */
        public void addToMaterialStash(ItemType type, int amount) {
            materialStash.put(type, materialStash.getOrDefault(type, 0) + amount);
        }

        /**
         * Get the number of items in the item stash.
         */
        public int getItemStashCount() {
            return itemStash.size();
        }

        /**
         * Get the total count of all materials in the material stash.
         */
        public int getMaterialStashCount() {
            return materialStash.values().stream().mapToInt(Integer::intValue).sum();
        }

        /**
         * Get the number of unique material types in the material stash.
         */
        public int getMaterialTypeCount() {
            return materialStash.size();
        }

        /**
         * Check if the item stash is at or above 90% capacity (648+ items).
         */
        public boolean isItemStashNearFull() {
            return itemStash.size() >= ITEM_STASH_WARNING_THRESHOLD;
        }

        /**
         * Check if the item stash is at its limit (720 items).
         */
        public boolean isItemStashFull() {
            return itemStash.size() >= ITEM_STASH_LIMIT;
        }

        /**
         * Remove an item from the item stash by index.
         * @param index The index of the item to remove
         * @return The removed item, or null if index is out of bounds
         */
        public SkyBlockItem removeFromItemStash(int index) {
            if (index < 0 || index >= itemStash.size()) {
                return null;
            }
            return itemStash.remove(index);
        }

        /**
         * Remove a specific amount of a material from the material stash.
         * @param type The item type
         * @param amount The amount to remove
         * @return The amount actually removed
         */
        public int removeFromMaterialStash(ItemType type, int amount) {
            int current = materialStash.getOrDefault(type, 0);
            int toRemove = Math.min(current, amount);
            if (toRemove <= 0) {
                return 0;
            }
            int remaining = current - toRemove;
            if (remaining <= 0) {
                materialStash.remove(type);
            } else {
                materialStash.put(type, remaining);
            }
            return toRemove;
        }

        /**
         * Check if the stash is empty (both item and material).
         */
        public boolean isEmpty() {
            return itemStash.isEmpty() && materialStash.isEmpty();
        }

        /**
         * Clear all items from both stashes.
         */
        public void clear() {
            itemStash.clear();
            materialStash.clear();
        }

        /**
         * Get the number of items that couldn't fit due to the limit.
         * @param attemptedCount The number of items attempted to add
         * @return The number of items that would overflow
         */
        public int getOverflowCount(int attemptedCount) {
            int availableSpace = ITEM_STASH_LIMIT - itemStash.size();
            return Math.max(0, attemptedCount - availableSpace);
        }
    }
}
