package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.ServerType;
import net.swofty.commons.YamlFileUtils;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FishingRegistry {
    private static final File FISHING_DIR = new File("./configuration/skyblock/fishing");

    private static final Map<String, FishingTableDefinition> TABLES = new LinkedHashMap<>();
    private static final Map<String, SeaCreatureDefinition> SEA_CREATURES = new LinkedHashMap<>();
    private static final Map<String, TrophyFishDefinition> TROPHY_FISH = new LinkedHashMap<>();
    private static final Map<String, HotspotDefinition> HOTSPOTS = new LinkedHashMap<>();

    private FishingRegistry() {
    }

    public static void loadAll() {
        TABLES.clear();
        SEA_CREATURES.clear();
        TROPHY_FISH.clear();
        HOTSPOTS.clear();

        if (!YamlFileUtils.ensureDirectoryExists(FISHING_DIR)) {
            throw new IllegalStateException("Unable to create fishing configuration directory");
        }

        try {
            loadTables(new File(FISHING_DIR, "tables.yml"));
            loadSeaCreatures(new File(FISHING_DIR, "sea_creatures.yml"));
            loadTrophyFish(new File(FISHING_DIR, "trophy_fish.yml"));
            loadHotspots(new File(FISHING_DIR, "hotspots.yml"));
        } catch (Exception exception) {
            Logger.error(exception, "Failed to load fishing configuration");
        }
    }

    public static @Nullable FishingTableDefinition getTable(String tableId) {
        return TABLES.get(tableId);
    }

    public static @Nullable TrophyFishDefinition getTrophyFish(String id) {
        return TROPHY_FISH.get(id);
    }

    public static @Nullable SeaCreatureDefinition getSeaCreature(String id) {
        return SEA_CREATURES.get(id);
    }


    public static List<FishingTableDefinition> getTables() {
        return List.copyOf(TABLES.values());
    }

    public static List<HotspotDefinition> getHotspots() {
        return List.copyOf(HOTSPOTS.values());
    }

    public static List<TrophyFishDefinition> getTrophyFish() {
        return List.copyOf(TROPHY_FISH.values());
    }

    public static List<SeaCreatureDefinition> getSeaCreatures() {
        return List.copyOf(SEA_CREATURES.values());
    }


    @SuppressWarnings("unchecked")
    private static void loadTables(File file) throws IOException {
        Map<String, Object> root = YamlFileUtils.loadYaml(file);
        List<Map<String, Object>> entries = (List<Map<String, Object>>) root.getOrDefault("tables", Collections.emptyList());
        for (Map<String, Object> entry : entries) {
            String id = string(entry, "id");
            TABLES.put(id, new FishingTableDefinition(
                id,
                stringList(entry.get("regions")),
                parseMediums((List<Object>) entry.get("mediums")),
                parseLootEntries((List<Map<String, Object>>) entry.get("items")),
                parseLootEntries((List<Map<String, Object>>) entry.get("treasures")),
                parseLootEntries((List<Map<String, Object>>) entry.get("junk")),
                parseSeaCreatureRolls((List<Map<String, Object>>) entry.get("seaCreatures"))
            ));
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadTrophyFish(File file) throws IOException {
        Map<String, Object> root = YamlFileUtils.loadYaml(file);
        List<Map<String, Object>> entries = (List<Map<String, Object>>) root.getOrDefault("trophyFish", Collections.emptyList());
        for (Map<String, Object> entry : entries) {
            String id = string(entry, "id");
            TROPHY_FISH.put(id, new TrophyFishDefinition(
                id,
                string(entry, "displayName"),
                doubleValue(entry, "catchChance", 0.0D),
                stringList(entry.get("regions")),
                intValue(entry, "requiredFishingLevel", 0),
                longValue(entry, "minimumCastTimeMs", 0L),
                nullableString(entry, "requiredRodId"),
                nullableDouble(entry.get("minimumMana")),
                nullableDouble(entry.get("minimumBobberDepth")),
                nullableDouble(entry.get("maximumPlayerDistance")),
                booleanValue(entry, "requiresStarterRodWithoutEnchantments", false),
                booleanValue(entry, "specialGoldenFish", false),
                nullableString(entry, "bronzeItemId"),
                nullableString(entry, "silverItemId"),
                nullableString(entry, "goldItemId"),
                nullableString(entry, "diamondItemId")
            ));
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadSeaCreatures(File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        Map<String, Object> root = YamlFileUtils.loadYaml(file);
        List<Map<String, Object>> entries = (List<Map<String, Object>>) root.getOrDefault("seaCreatures", Collections.emptyList());
        for (Map<String, Object> entry : entries) {
            String id = string(entry, "id");
            SEA_CREATURES.put(id, new SeaCreatureDefinition(
                id,
                string(entry, "displayName"),
                intValue(entry, "requiredFishingLevel", 0),
                doubleValue(entry, "skillXp", 0.0D),
                stringList(entry.get("tags")),
                nullableString(entry, "corruptedVariantId")
            ));
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadHotspots(File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        Map<String, Object> root = YamlFileUtils.loadYaml(file);
        List<Map<String, Object>> entries = (List<Map<String, Object>>) root.getOrDefault("hotspots", Collections.emptyList());
        int generatedId = 1;
        for (Map<String, Object> entry : entries) {
            if (entry.containsKey("x") && entry.containsKey("y") && entry.containsKey("z")) {
                String serverType = normalizeServerType(nullableString(entry, "serverType"));
                String region = nullableString(entry, "region");
                HOTSPOTS.put("POSITION_" + generatedId, new HotspotDefinition(
                    "POSITION_" + generatedId,
                    "Hotspot",
                    region == null ? List.of() : List.of(region.toUpperCase()),
                    enumValue(entry, "medium", FishingMedium.class, FishingMedium.WATER),
                    intValue(entry, "maxActive", defaultMaxActiveForServer(serverType)),
                    intValue(entry, "durationSeconds", 120),
                    parseStatistics((Map<String, Object>) entry.get("buffs"), defaultHotspotBuffs()),
                    stringList(entry.get("seaCreatures")),
                    List.of(new HotspotDefinition.SpawnPoint(
                        doubleValue(entry.get("x"), 0),
                        doubleValue(entry.get("y"), 0),
                        doubleValue(entry.get("z"), 0),
                        serverType
                    ))
                ));
                generatedId++;
                continue;
            }

            String id = string(entry, "id");
            HOTSPOTS.put(id, new HotspotDefinition(
                id,
                string(entry, "displayName"),
                stringList(entry.get("regions")),
                enumValue(entry, "medium", FishingMedium.class, FishingMedium.WATER),
                intValue(entry, "maxActive", 1),
                intValue(entry, "durationSeconds", 120),
                parseStatistics((Map<String, Object>) entry.get("buffs"), defaultHotspotBuffs()),
                stringList(entry.get("seaCreatures")),
                parseHotspotSpawnPoints((List<Map<String, Object>>) entry.get("positions"), nullableString(entry, "serverType"))
            ));
        }
    }


    private static ItemStatistics parseStatistics(@Nullable Map<String, Object> values) {
        return parseStatistics(values, Map.of());
    }

    private static ItemStatistics parseStatistics(@Nullable Map<String, Object> values, Map<String, Double> defaults) {
        if (values == null || values.isEmpty()) {
            if (defaults.isEmpty()) {
                return ItemStatistics.empty();
            }
            values = new LinkedHashMap<>();
        }

        Map<String, Object> mergedValues = new LinkedHashMap<>();
        defaults.forEach(mergedValues::put);
        mergedValues.putAll(values);

        if (mergedValues.isEmpty()) {
            return ItemStatistics.empty();
        }

        ItemStatistics.Builder builder = ItemStatistics.builder();
        for (Map.Entry<String, Object> entry : mergedValues.entrySet()) {
            builder.withBase(ItemStatistic.valueOf(entry.getKey().toUpperCase()), doubleValue(entry.getValue(), 0.0D));
        }
        return builder.build();
    }


    private static List<FishingMedium> parseMediums(@Nullable List<Object> mediums) {
        if (mediums == null || mediums.isEmpty()) {
            return List.of(FishingMedium.WATER);
        }

        List<FishingMedium> result = new ArrayList<>();
        for (Object value : mediums) {
            result.add(FishingMedium.valueOf(String.valueOf(value).toUpperCase()));
        }
        return result;
    }

    private static List<MobType> parseMobTypes(@Nullable List<Object> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }

        List<MobType> mobTypes = new ArrayList<>();
        for (Object value : values) {
            mobTypes.add(MobType.valueOf(String.valueOf(value).toUpperCase()));
        }
        return mobTypes;
    }

    private static List<HotspotDefinition.SpawnPoint> parseHotspotSpawnPoints(@Nullable List<Map<String, Object>> entries, @Nullable String fallbackServerType) {
        if (entries == null || entries.isEmpty()) {
            return List.of();
        }

        List<HotspotDefinition.SpawnPoint> points = new ArrayList<>();
        for (Map<String, Object> entry : entries) {
            points.add(new HotspotDefinition.SpawnPoint(
                doubleValue(entry.get("x"), 0),
                doubleValue(entry.get("y"), 0),
                doubleValue(entry.get("z"), 0),
                normalizeServerType(nullableString(entry, "serverType"), fallbackServerType)
            ));
        }
        return points;
    }

    private static String normalizeServerType(@Nullable String serverType) {
        return normalizeServerType(serverType, null);
    }

    private static String normalizeServerType(@Nullable String serverType, @Nullable String fallback) {
        String value = serverType == null || serverType.isEmpty() ? fallback : serverType;
        if (value == null || value.isEmpty()) {
            return ServerType.SKYBLOCK_HUB.name();
        }
        return ServerType.getSkyblockServer(value).name();
    }

    private static int defaultMaxActiveForServer(String serverType) {
        return switch (serverType) {
            case "SKYBLOCK_HUB", "SKYBLOCK_BACKWATER_BAYOU" -> 2;
            case "SKYBLOCK_SPIDERS_DEN" -> 1;
            case "SKYBLOCK_CRIMSON_ISLE" -> 3;
            default -> 1;
        };
    }

    private static Map<String, Double> defaultHotspotBuffs() {
        Map<String, Double> defaults = new LinkedHashMap<>();
        defaults.put("FISHING_SPEED", 15.0D);
        defaults.put("SEA_CREATURE_CHANCE", 5.0D);
        defaults.put("DOUBLE_HOOK_CHANCE", 2.0D);
        defaults.put("TROPHY_FISH_CHANCE", 5.0D);
        defaults.put("TREASURE_CHANCE", 1.0D);
        return defaults;
    }

    private static List<FishingTableDefinition.LootEntry> parseLootEntries(@Nullable List<Map<String, Object>> entries) {
        if (entries == null || entries.isEmpty()) {
            return List.of();
        }

        List<FishingTableDefinition.LootEntry> result = new ArrayList<>();
        for (Map<String, Object> entry : entries) {
            result.add(new FishingTableDefinition.LootEntry(
                string(entry, "itemId"),
                doubleValue(entry, "chance", 0.0D),
                intValue(entry, "amount", 1),
                doubleValue(entry, "skillXp", 0.0D)
            ));
        }
        return result;
    }

    private static List<FishingTableDefinition.SeaCreatureRoll> parseSeaCreatureRolls(@Nullable List<Map<String, Object>> entries) {
        if (entries == null || entries.isEmpty()) {
            return List.of();
        }

        List<FishingTableDefinition.SeaCreatureRoll> result = new ArrayList<>();
        for (Map<String, Object> entry : entries) {
            result.add(new FishingTableDefinition.SeaCreatureRoll(
                string(entry, "seaCreatureId"),
                doubleValue(entry, "chance", 0.0D)
            ));
        }
        return result;
    }

    private static Map<String, Double> parseStringDoubleMap(@Nullable Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            return Map.of();
        }

        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            result.put(entry.getKey(), doubleValue(entry.getValue(), 0.0D));
        }
        return result;
    }

    private static String string(Map<String, Object> entry, String key) {
        Object value = entry.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing fishing config key: " + key);
        }
        return String.valueOf(value);
    }

    private static @Nullable String nullableString(Map<String, Object> entry, String key) {
        Object value = entry.get(key);
        if (value == null) {
            return null;
        }
        String stringValue = String.valueOf(value);
        return stringValue.isEmpty() ? null : stringValue;
    }

    private static List<String> stringList(@Nullable Object raw) {
        if (!(raw instanceof List<?> list)) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (Object entry : list) {
            values.add(String.valueOf(entry));
        }
        return values;
    }

    private static int intValue(Map<String, Object> entry, String key, int defaultValue) {
        return intValue(entry.get(key), defaultValue);
    }

    private static int intValue(@Nullable Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private static long longValue(Map<String, Object> entry, String key, long defaultValue) {
        return longValue(entry.get(key), defaultValue);
    }

    private static long longValue(@Nullable Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private static double doubleValue(Map<String, Object> entry, String key, double defaultValue) {
        return doubleValue(entry.get(key), defaultValue);
    }

    private static double doubleValue(@Nullable Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        return Double.parseDouble(String.valueOf(value));
    }

    private static @Nullable Double nullableDouble(@Nullable Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        String stringValue = String.valueOf(value);
        return stringValue.isEmpty() ? null : Double.parseDouble(stringValue);
    }

    private static boolean booleanValue(Map<String, Object> entry, String key, boolean defaultValue) {
        Object value = entry.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    private static <T extends Enum<T>> T enumValue(Map<String, Object> entry, String key, Class<T> type, T defaultValue) {
        Object value = entry.get(key);
        if (value == null) {
            return defaultValue;
        }
        return Enum.valueOf(type, String.valueOf(value).toUpperCase());
    }
}
