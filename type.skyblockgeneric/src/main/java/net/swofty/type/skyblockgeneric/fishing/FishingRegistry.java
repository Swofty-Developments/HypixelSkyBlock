package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.ServerType;
import net.swofty.commons.YamlFileUtils;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
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
    private static final Map<String, TrophyFishDefinition> TROPHY_FISH = new LinkedHashMap<>();
    private static final Map<String, SeaCreatureDefinition> SEA_CREATURES = new LinkedHashMap<>();
    private static final Map<String, HotspotDefinition> HOTSPOTS = new LinkedHashMap<>();

    private FishingRegistry() {
    }

    public static void loadAll() {
        TABLES.clear();
        TROPHY_FISH.clear();
        SEA_CREATURES.clear();
        HOTSPOTS.clear();

        if (!YamlFileUtils.ensureDirectoryExists(FISHING_DIR)) {
            throw new IllegalStateException("Unable to create fishing configuration directory");
        }

        try {
            loadTables(new File(FISHING_DIR, "tables.yml"));
            loadTrophyFish(new File(FISHING_DIR, "trophy_fish.yml"));
            loadSeaCreatures(new File(FISHING_DIR, "sea_creatures.yml"));
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
        if (!file.exists()) {
            return;
        }
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
                intValue(entry, "requiredFishingLevel", 0),
                doubleValue(entry, "skillXp", 0.0D),
                stringList(entry.get("tags"))
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
        for (Map<String, Object> entry : entries) {
            String serverType = normalizeServerType(nullableString(entry, "serverType"));
            List<Map<String, Object>> rawSpawnPoints = (List<Map<String, Object>>) entry.get("spawnPoints");
            List<HotspotDefinition.SpawnPoint> spawnPoints = new ArrayList<>();
            if (rawSpawnPoints == null || rawSpawnPoints.isEmpty()) {
                spawnPoints.add(new HotspotDefinition.SpawnPoint(
                    serverType,
                    doubleValue(entry.get("x"), 0.0D),
                    doubleValue(entry.get("y"), 0.0D),
                    doubleValue(entry.get("z"), 0.0D)
                ));
            } else {
                for (Map<String, Object> spawnPoint : rawSpawnPoints) {
                    spawnPoints.add(new HotspotDefinition.SpawnPoint(
                        normalizeServerType(nullableString(spawnPoint, "serverType"), serverType),
                        doubleValue(spawnPoint.get("x"), 0.0D),
                        doubleValue(spawnPoint.get("y"), 0.0D),
                        doubleValue(spawnPoint.get("z"), 0.0D)
                    ));
                }
            }

            String id = nullableString(entry, "id");
            if (id == null || id.isBlank()) {
                id = serverType + "_" + HOTSPOTS.size();
            }

            Map<String, Object> buffs = castMap(entry.get("buffs"));
            HOTSPOTS.put(id, new HotspotDefinition(
                id,
                nullableString(entry, "displayName") == null ? "Hotspot" : nullableString(entry, "displayName"),
                stringList(entry.get("regions")),
                entry.containsKey("medium") ? FishingMedium.valueOf(String.valueOf(entry.get("medium")).toUpperCase()) : FishingMedium.WATER,
                intValue(entry, "maxActive", defaultMaxActiveForServer(serverType)),
                intValue(entry, "durationSeconds", 120),
                parseStatistics(buffs, defaultHotspotBuffs()),
                stringList(entry.get("seaCreatures")),
                spawnPoints
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

    @SuppressWarnings("unchecked")
    private static @Nullable Map<String, Object> castMap(@Nullable Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return null;
    }

    private static List<String> stringList(@Nullable Object value) {
        if (!(value instanceof List<?> values) || values.isEmpty()) {
            return List.of();
        }

        List<String> result = new ArrayList<>();
        for (Object entry : values) {
            result.add(String.valueOf(entry));
        }
        return result;
    }

    private static String string(Map<String, Object> values, String key) {
        String value = nullableString(values, key);
        if (value == null) {
            throw new IllegalArgumentException("Missing fishing config key: " + key);
        }
        return value;
    }

    private static @Nullable String nullableString(Map<String, Object> values, String key) {
        return nullableString(values.get(key));
    }

    private static @Nullable String nullableString(@Nullable Object value) {
        if (value == null) {
            return null;
        }
        String stringValue = String.valueOf(value);
        return stringValue.isBlank() ? null : stringValue;
    }

    private static int intValue(Map<String, Object> values, String key, int defaultValue) {
        return intValue(values.get(key), defaultValue);
    }

    private static int intValue(@Nullable Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return ((Number) value).intValue();
    }

    private static long longValue(Map<String, Object> values, String key, long defaultValue) {
        return longValue(values.get(key), defaultValue);
    }

    private static long longValue(@Nullable Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return ((Number) value).longValue();
    }

    private static double doubleValue(Map<String, Object> values, String key, double defaultValue) {
        return doubleValue(values.get(key), defaultValue);
    }

    private static double doubleValue(@Nullable Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return ((Number) value).doubleValue();
    }

    private static @Nullable Double nullableDouble(@Nullable Object value) {
        if (value == null) {
            return null;
        }
        return ((Number) value).doubleValue();
    }

    private static boolean booleanValue(Map<String, Object> values, String key, boolean defaultValue) {
        return booleanValue(values.get(key), defaultValue);
    }

    private static boolean booleanValue(@Nullable Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
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
}
