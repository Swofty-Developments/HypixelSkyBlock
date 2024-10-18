package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.TieredTalisman;
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
                        entry.getValue().toUnderstandable().serialize());
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public DatapointAccessoryBag.PlayerAccessoryBag deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Integer, SkyBlockItem> map = new HashMap<>();
            List<ItemTypeLinker> discoveredAccessories = new ArrayList<>();

            for (String key : obj.keySet()) {
                if (key.equals("discoveredAccessories")) {
                    if (obj.isNull(key)) continue;
                    if (obj.get(key) instanceof String) continue;
                    if (obj.getJSONArray(key).isEmpty()) continue;
                    for (Object o : obj.getJSONArray(key)) {
                        discoveredAccessories.add(ItemTypeLinker.valueOf(o.toString()));
                    }
                    continue;
                }

                map.put(Integer.parseInt(key),
                        new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(obj.getString(key))));
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
        private List<ItemTypeLinker> discoveredAccessories = new ArrayList<>();

        /**
         * Retrieves the accessory item located in the specified slot.
         *
         * @param slot the slot index of the accessory
         * @return the {@link SkyBlockItem} in the specified slot, or {@code null} if no item exists
         */
        public @Nullable SkyBlockItem getInSlot(int slot) {
            return accessoryMap.get(slot);
        }

        /**
         * Retrieves a list of all accessories currently in the accessory map.
         *
         * @return an unmodifiable list of {@link SkyBlockItem} representing all accessories
         */
        public List<SkyBlockItem> getAllAccessories() {
            return List.copyOf(accessoryMap.values());
        }

        /**
         * Retrieves a list of unique accessories, ensuring that only the highest tier version
         * of each accessory type is included.
         *
         * @return an unmodifiable list of unique {@link SkyBlockItem} accessories
         */
        public List<SkyBlockItem> getUniqueAccessories() {
            List<SkyBlockItem> accessories = new ArrayList<>(getAllAccessories());
            Map<ItemTypeLinker, SkyBlockItem> highestTierTalismans = new HashMap<>();

            for (SkyBlockItem accessory : accessories) {
                if (accessory.getGenericInstance() instanceof TieredTalisman currentTalisman) {
                    ItemTypeLinker baseTalisman = currentTalisman.getBaseTalismanTier();
                    TieredTalisman tieredTalisman = highestTierTalismans.containsKey(baseTalisman) ? (TieredTalisman) highestTierTalismans.get(baseTalisman).getGenericInstance() : null;

                    // Update the highest tier talisman if necessary
                    if (tieredTalisman == null || tieredTalisman.getTier() < currentTalisman.getTier()) {
                        highestTierTalismans.put(baseTalisman, accessory);
                    }
                } else if (!accessory.isAir()) {
                    highestTierTalismans.put(accessory.getAttributeHandler().getPotentialClassLinker(), accessory);
                }
            }
            return List.copyOf(highestTierTalismans.values());
        }

        /**
         * Retrieves a list of unique accessories filtered by the specified rarity.
         *
         * @param rarity the {@link Rarity} to filter the accessories by
         * @return an unmodifiable list of unique {@link SkyBlockItem} accessories matching the specified rarity
         */
        public List<SkyBlockItem> getUniqueAccessories(Rarity rarity) {
            List<SkyBlockItem> talismans = new ArrayList<>();
            for (SkyBlockItem item : getUniqueAccessories()) {
                if (rarity == item.getAttributeHandler().getRarity()) {
                    talismans.add(item);
                }
            }
            return talismans;
        }

        /**
         * Adds a discovered accessory type to the list of discovered accessories.
         *
         * @param type the {@link ItemTypeLinker} representing the accessory type to add
         */
        public void addDiscoveredAccessory(ItemTypeLinker type) {
            if (discoveredAccessories.contains(type)) return;
            discoveredAccessories.add(type);
        }

        /**
         * Checks if the specified accessory type has been discovered.
         *
         * @param type the {@link ItemTypeLinker} representing the accessory type to check
         * @return {@code true} if the accessory type has been discovered; {@code false} otherwise
         */
        public boolean hasDiscoveredAccessory(ItemTypeLinker type) {
            return discoveredAccessories.contains(type);
        }

        /**
         * Removes the accessory item from the specified slot.
         *
         * @param slot the slot index of the accessory to remove
         */
        public void removeFromSlot(int slot) {
            accessoryMap.remove(slot);
        }

        /**
         * Sets the specified accessory item in the given slot.
         *
         * @param slot the slot index where the accessory should be placed
         * @param item the {@link SkyBlockItem} to place in the specified slot
         */
        public void setInSlot(int slot, SkyBlockItem item) {
            accessoryMap.put(slot, item);
        }
    }
}
