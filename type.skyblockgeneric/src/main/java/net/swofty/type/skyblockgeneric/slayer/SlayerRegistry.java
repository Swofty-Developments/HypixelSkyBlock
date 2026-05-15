package net.swofty.type.skyblockgeneric.slayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.swofty.commons.YamlFileUtils;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

public final class SlayerRegistry {
    private static final File SLAYERS_FILE = new File("./configuration/skyblock/slayers.yml");
    private static final Map<SlayerType, SlayerDefinition> DEFINITIONS = new LinkedHashMap<>();

    private SlayerRegistry() {
    }

    public static void loadAll() {
        DEFINITIONS.clear();

        try {
            Map<String, Object> root = YamlFileUtils.loadYaml(SLAYERS_FILE);
            List<Map<String, Object>> slayers = (List<Map<String, Object>>) root.getOrDefault("slayers", Collections.emptyList());
            for (Map<String, Object> entry : slayers) {
                SlayerType type = SlayerType.valueOf(string(entry, "id"));
                DEFINITIONS.put(type, new SlayerDefinition(
                    type,
                    booleanValue(entry, "enabled", true),
                    unlock((Map<String, Object>) entry.get("unlock")),
                    mobTypes((List<Object>) entry.getOrDefault("targetMobTypes", Collections.emptyList())),
                    levels((List<Map<String, Object>>) entry.getOrDefault("levels", Collections.emptyList())),
                    tiers((List<Map<String, Object>>) entry.getOrDefault("tiers", Collections.emptyList()))
                ));
            }
        } catch (Exception exception) {
            Logger.error(exception, "Failed to load slayer configuration");
        }
    }

    public static @Nullable SlayerDefinition get(SlayerType type) {
        return DEFINITIONS.get(type);
    }

    public static List<SlayerDefinition> all() {
        return List.copyOf(DEFINITIONS.values());
    }

    private static List<MobType> mobTypes(List<Object> values) {
        List<MobType> result = new ArrayList<>();
        for (Object value : values) {
            result.add(MobType.valueOf(String.valueOf(value)));
        }
        return result;
    }

    private static Optional<SlayerUnlockRequirement> unlock(@Nullable Map<String, Object> entry) {
        if (entry == null || entry.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new SlayerUnlockRequirement(
            SlayerType.valueOf(string(entry, "type")),
            SlayerTier.valueOf(string(entry, "tier"))
        ));
    }

    private static List<SlayerLevelReward> levels(List<Map<String, Object>> entries) {
        List<SlayerLevelReward> result = new ArrayList<>();
        for (Map<String, Object> entry : entries) {
            result.add(new SlayerLevelReward(
                intValue(entry, "level", 0),
                intValue(entry, "requiredXp", 0),
                string(entry, "title")
            ));
        }
        return result;
    }

    private static Map<SlayerTier, SlayerTierDefinition> tiers(List<Map<String, Object>> entries) {
        Map<SlayerTier, SlayerTierDefinition> result = new EnumMap<>(SlayerTier.class);
        for (Map<String, Object> entry : entries) {
            SlayerTier tier = SlayerTier.valueOf(string(entry, "tier"));
            result.put(tier, new SlayerTierDefinition(
                tier,
                intValue(entry, "cost", 0),
                intValue(entry, "requiredCombatXp", 0),
                intValue(entry, "slayerXp", 0),
                intValue(entry, "bossLevel", 1),
                doubleValue(entry, "bossHealth", 100D),
                doubleValue(entry, "bossDamage", 5D),
                doubleValue(entry, "bossSpeed", 100D),
                intValue(entry, "tokenDrops", 0),
                optionalItem(entry.get("tokenItem"))
            ));
        }
        return result;
    }

    private static Optional<ItemType> optionalItem(Object value) {
        if (value == null || String.valueOf(value).isBlank()) {
            return Optional.empty();
        }
        return Optional.of(ItemType.valueOf(String.valueOf(value)));
    }

    private static String string(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required slayer key '" + key + "'");
        }
        return String.valueOf(value);
    }

    private static int intValue(Map<String, Object> map, String key, int fallback) {
        Object value = map.get(key);
        if (value == null) return fallback;
        if (value instanceof Number number) return number.intValue();
        return Integer.parseInt(String.valueOf(value));
    }

    private static double doubleValue(Map<String, Object> map, String key, double fallback) {
        Object value = map.get(key);
        if (value == null) return fallback;
        if (value instanceof Number number) return number.doubleValue();
        return Double.parseDouble(String.valueOf(value));
    }

    private static boolean booleanValue(Map<String, Object> map, String key, boolean fallback) {
        Object value = map.get(key);
        if (value == null) return fallback;
        if (value instanceof Boolean bool) return bool;
        return Boolean.parseBoolean(String.valueOf(value));
    }
}
