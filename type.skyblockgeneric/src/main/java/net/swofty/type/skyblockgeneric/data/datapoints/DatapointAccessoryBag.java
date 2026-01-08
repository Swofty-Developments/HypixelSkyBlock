package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.TieredTalismansRegistry;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointAccessoryBag extends SkyBlockDatapoint<DatapointAccessoryBag.PlayerAccessoryBag> {
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
        private List<ItemType> discoveredAccessories = new ArrayList<>();

        public @Nullable SkyBlockItem getInSlot(int slot) {
            return accessoryMap.get(slot);
        }

        public List<SkyBlockItem> getAllAccessories() {
            return List.copyOf(accessoryMap.values());
        }

        public List<SkyBlockItem> getUniqueAccessories() {
            List<SkyBlockItem> accessories = new ArrayList<>(getAllAccessories());
            Map<TieredTalismansRegistry, SkyBlockItem> highestTierTalismans = new HashMap<>();
            Map<ItemType, SkyBlockItem> nonTieredAccessories = new HashMap<>();

            for (SkyBlockItem accessory : accessories) {
                ItemType itemType = accessory.getAttributeHandler().getPotentialType();
                TieredTalismansRegistry talismanGroup = TieredTalismansRegistry.getTieredTalisman(itemType);

                if (talismanGroup != null) {
                    if (talismanGroup.isTiered()) {
                        SkyBlockItem currentBest = highestTierTalismans.get(talismanGroup);
                        ItemType currentBestType = currentBest != null ? currentBest.getAttributeHandler().getPotentialType() : null;

                        int currentTier = currentBestType != null ? TieredTalismansRegistry.getTier(currentBestType) : -1;
                        int newTier = TieredTalismansRegistry.getTier(itemType);

                        if (currentBest == null || newTier > currentTier) {
                            highestTierTalismans.put(talismanGroup, accessory);
                        }
                    } else {
                        nonTieredAccessories.put(itemType, accessory);
                    }
                } else if (!accessory.isAir()) {
                    nonTieredAccessories.put(itemType, accessory);
                }
            }

            List<SkyBlockItem> result = new ArrayList<>(highestTierTalismans.values());
            result.addAll(nonTieredAccessories.values());

            return result;
        }

        public List<SkyBlockItem> getUniqueAccessories(Rarity rarity) {
            List<SkyBlockItem> talismans = new ArrayList<>(List.of());
            for (SkyBlockItem item : getUniqueAccessories()) {
                if (rarity == item.getAttributeHandler().getRarity()) {
                    talismans.add(item);
                }
            }
            return talismans;
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
