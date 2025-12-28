package net.swofty.type.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatapointBedWarsQuickBuy extends Datapoint<DatapointBedWarsQuickBuy.PlayerQuickBuy> {
    public static Serializer<PlayerQuickBuy> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerQuickBuy value) {
            Map<String, String> serialized = new HashMap<>();

            for (Map.Entry<Integer, String> entry : value.getQuickBuyMap().entrySet()) {
                serialized.put(String.valueOf(entry.getKey()), entry.getValue());
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerQuickBuy deserialize(String json) {
            if (json == null || !json.startsWith("{")) {
                return new PlayerQuickBuy(new HashMap<>(DEFAULT_QUICK_BUY));
            }

            try {
                JSONObject obj = new JSONObject(json);
                Map<Integer, String> quickBuyMap = new HashMap<>();

                for (String key : obj.keySet()) {
                    String itemId = obj.getString(key);
                    if (itemId != null && !itemId.isEmpty()) {
                        quickBuyMap.put(Integer.parseInt(key), itemId);
                    }
                }

                return new PlayerQuickBuy(quickBuyMap);
            } catch (Exception e) {
                return new PlayerQuickBuy(new HashMap<>(DEFAULT_QUICK_BUY));
            }
        }

        @Override
        public PlayerQuickBuy clone(PlayerQuickBuy value) {
            return new PlayerQuickBuy(new HashMap<>(value.getQuickBuyMap()));
        }
    };

    private static final Map<Integer, String> DEFAULT_QUICK_BUY = Map.ofEntries(
            Map.entry(0, "wool"),
            Map.entry(1, "stone_sword"),
            Map.entry(2, "chainmail_armor"),
            Map.entry(3, "pickaxe"),
            Map.entry(4, "bow_1"),
            Map.entry(5, "invisibility_potion"),
            Map.entry(6, "tnt"),
            Map.entry(7, "wood"),
            Map.entry(8, "iron_sword"),
            Map.entry(9, "iron_armor"),
            Map.entry(10, "shears"),
            Map.entry(11, "arrow"),
            Map.entry(12, "speed_potion"),
            Map.entry(13, "water_bucket"),
            Map.entry(14, "golden_apple"),
            Map.entry(15, "jump_potion"),
            Map.entry(16, "glass"),
            Map.entry(17, "endstone"),
            Map.entry(18, "axe")
    );

    public DatapointBedWarsQuickBuy(String key, PlayerQuickBuy value) {
        super(key, value, serializer);
    }

    public DatapointBedWarsQuickBuy(String key) {
        super(key, new PlayerQuickBuy(new HashMap<>(DEFAULT_QUICK_BUY)), serializer);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerQuickBuy {
        private Map<Integer, String> quickBuyMap = new HashMap<>();

        /**
         * Gets the item ID at the specified slot.
         *
         * @param slot the slot index (0-20)
         * @return the item ID, or null if the slot is empty
         */
        public @Nullable String getItemId(int slot) {
            return quickBuyMap.get(slot);
        }

        /**
         * Sets the item ID at the specified slot.
         *
         * @param slot   the slot index (0-20)
         * @param itemId the item ID to set
         */
        public void setItemId(int slot, String itemId) {
            if (slot < 0 || slot > 20) {
                throw new IllegalArgumentException("Quick buy slot must be between 0 and 20");
            }
            if (itemId == null || itemId.isEmpty()) {
                quickBuyMap.remove(slot);
            } else {
                quickBuyMap.put(slot, itemId);
            }
        }

        public void removeFromSlot(int slot) {
            quickBuyMap.remove(slot);
        }

        public Set<Integer> getOccupiedSlots() {
            return quickBuyMap.keySet();
        }

        public boolean containsItemId(String itemId) {
            return quickBuyMap.containsValue(itemId);
        }

        public int findSlotByItemId(String itemId) {
            for (Map.Entry<Integer, String> entry : quickBuyMap.entrySet()) {
                if (itemId.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
            return -1;
        }

    }
}
