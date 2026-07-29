package net.swofty.type.generic.data.datapoints;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointBedWarsHotbar extends Datapoint<Map<Byte, DatapointBedWarsHotbar.HotbarItemType>> {
    public static Serializer<Map<Byte, DatapointBedWarsHotbar.HotbarItemType>> serializer = new Serializer<>() {
        @Override
        public String serialize(Map<Byte, HotbarItemType> value) {
            Map<String, String> serialized = new HashMap<>();
            for (Map.Entry<Byte, HotbarItemType> entry : value.entrySet()) {
                serialized.put(String.valueOf(entry.getKey()), entry.getValue().name());
            }
            return new JSONObject(serialized).toString();
        }

        @Override
        public Map<Byte, HotbarItemType> deserialize(String json) {
            if (json == null || !json.startsWith("{")) {
                return defaultHotbar;
            }

            try {
                JSONObject obj = new JSONObject(json);
                Map<Byte, HotbarItemType> hotbarMap = new HashMap<>();

                for (String key : obj.keySet()) {
                    String itemTypeStr = obj.getString(key);
                    if (itemTypeStr != null && !itemTypeStr.isEmpty()) {
                        try {
                            HotbarItemType itemType = HotbarItemType.valueOf(itemTypeStr);
                            hotbarMap.put(Byte.parseByte(key), itemType);
                        } catch (IllegalArgumentException e) {
                            // Skip invalid item types
                        }
                    }
                }

                return hotbarMap;
            } catch (Exception e) {
                return defaultHotbar;
            }
        }

        @Override
        public Map<Byte, HotbarItemType> clone(Map<Byte, HotbarItemType> value) {
            return new HashMap<>(value);
        }

    };

    public DatapointBedWarsHotbar(String key, HashMap<Byte, HotbarItemType> value) {
        super(key, value, serializer);
    }

    public DatapointBedWarsHotbar(String key) {
        super(key, defaultHotbar, serializer);
    }

    public static Map<Byte, HotbarItemType> defaultHotbar =
        new HashMap<>(Map.of(
            (byte) 0, HotbarItemType.MELEE,
            (byte) 8, HotbarItemType.COMPASS
        ));

    @Getter
    public enum HotbarItemType {
        BLOCKS(Material.TERRACOTTA),
        MELEE(Material.GOLDEN_SWORD),
        PICKAXE(Material.IRON_PICKAXE),
        AXE(Material.IRON_AXE),
        SHEARS(Material.SHEARS),
        RANGED(Material.BOW),
        POTIONS(Material.POTION),
        UTILITY(Material.TNT),
        COMPASS(Material.COMPASS);

        private final Material material;

        HotbarItemType(Material material) {
            this.material = material;
        }

        @Nullable
        public static HotbarItemType fromOrdinal(int ordinal) {
            for (HotbarItemType type : values()) {
                if (type.ordinal() == ordinal) {
                    return type;
                }
            }
            return null;
        }

        public String pretty() {
            return StringUtility.capitalize(this.name().toLowerCase());
        }
    }

}
