package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.item.Rarity;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.protocol.serializers.SkyBlockItemDeserializer;
import net.swofty.commons.protocol.serializers.SkyBlockItemSerializer;
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
    public static Serializer<PlayerAccessoryBag> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerAccessoryBag value) {
            Map<String, String> serialized = new HashMap<>();

            serialized.put("discoveredAccessories", new JSONObject(value.getDiscoveredAccessories()).toString());

            for (Map.Entry<Integer, SkyBlockItem> entry : value.getAccessoryMap().entrySet()) {
                serialized.put(String.valueOf(entry.getKey()),
                        SkyBlockItemSerializer.serializeJSON(entry.getValue().toUnderstandable()).toString());
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerAccessoryBag deserialize(String json) {
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
                        new SkyBlockItem(SkyBlockItemDeserializer.deserializeJSON(
                                new JSONObject(obj.getString(key))
                        )));
            }

            return new PlayerAccessoryBag(map, discoveredAccessories);
        }

        @Override
        public PlayerAccessoryBag clone(PlayerAccessoryBag value) {
            return new PlayerAccessoryBag(value.getAccessoryMap(), value.getDiscoveredAccessories());
        }
    };

    public DatapointAccessoryBag(String key, PlayerAccessoryBag value) {
        super(key, value, serializer);
    }

    public DatapointAccessoryBag(String key) {
        super(key, new PlayerAccessoryBag(), serializer);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerAccessoryBag {
        private Map<Integer, SkyBlockItem> accessoryMap = new HashMap<>();
        private List<ItemTypeLinker> discoveredAccessories = new ArrayList<>();

        public @Nullable SkyBlockItem getInSlot(int slot) {
            return accessoryMap.get(slot);
        }

        public List<SkyBlockItem> getAllAccessories() {
            return List.copyOf(accessoryMap.values());
        }

        public List<SkyBlockItem> getUniqueAccessories() {
            List<SkyBlockItem> accessories = new ArrayList<>(getAllAccessories());
            Map<ItemTypeLinker, SkyBlockItem> highestTierTalismans = new HashMap<>();

            for (SkyBlockItem accessory : accessories) {
                if (accessory.getGenericInstance() instanceof TieredTalisman currentTalisman) {
                    ItemTypeLinker baseTalisman = currentTalisman.getBaseTalismanTier();
                    TieredTalisman tieredTalisman = highestTierTalismans.containsKey(baseTalisman) ? (TieredTalisman) highestTierTalismans.get(baseTalisman).getGenericInstance() : null;

                    if (tieredTalisman == null || tieredTalisman.getTier() < currentTalisman.getTier()) {
                        highestTierTalismans.put(baseTalisman, accessory);
                    }
                } else if (!accessory.isAir()) {
                    highestTierTalismans.put(accessory.getAttributeHandler().getPotentialClassLinker(), accessory);
                }
            }
            return List.copyOf(highestTierTalismans.values());
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

        public void addDiscoveredAccessory(ItemTypeLinker type) {
            if (discoveredAccessories.contains(type)) return;
            discoveredAccessories.add(type);
        }

        public boolean hasDiscoveredAccessory(ItemTypeLinker type) {
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
