package net.swofty.type.ravengardgeneric.texturepack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minestom.server.instance.block.Block;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BlockColorMapping {
    private static final Map<String, Byte> BLOCK_COLORS = new HashMap<>();
    private static final Map<String, Byte> AUTO_STATE_COLORS = new HashMap<>();
    private static final Map<String, Byte> AUTO_DEFAULT_COLORS = new HashMap<>();

    static {
        color("minecraft:grass_block", 1, 2);
        color("minecraft:short_grass", 1, 2);
        color("minecraft:tall_grass", 1, 2);
        color("minecraft:fern", 1, 2);
        color("minecraft:large_fern", 1, 2);

        color("minecraft:sand", 2, 2);
        color("minecraft:sandstone", 2, 2);
        color("minecraft:red_sand", 10, 2);

        color("minecraft:cobweb", 3, 2);

        color("minecraft:lava", 4, 2);
        color("minecraft:fire", 4, 2);
        color("minecraft:magma_block", 4, 2);

        color("minecraft:ice", 5, 2);
        color("minecraft:packed_ice", 5, 2);
        color("minecraft:blue_ice", 5, 2);

        color("minecraft:iron_block", 6, 2);

        color("minecraft:oak_leaves", 7, 2);
        color("minecraft:birch_leaves", 7, 2);
        color("minecraft:spruce_leaves", 7, 2);
        color("minecraft:jungle_leaves", 7, 2);
        color("minecraft:acacia_leaves", 7, 2);
        color("minecraft:dark_oak_leaves", 7, 2);
        color("minecraft:mangrove_leaves", 7, 2);

        color("minecraft:white_wool", 8, 2);
        color("minecraft:snow_block", 8, 2);
        color("minecraft:snow", 8, 2);

        color("minecraft:clay", 9, 2);

        color("minecraft:dirt", 10, 2);
        color("minecraft:coarse_dirt", 10, 2);
        color("minecraft:farmland", 10, 2);
        color("minecraft:mud", 10, 2);

        color("minecraft:stone", 11, 2);
        color("minecraft:cobblestone", 11, 2);
        color("minecraft:andesite", 11, 2);
        color("minecraft:diorite", 11, 2);
        color("minecraft:granite", 11, 2);
        color("minecraft:gravel", 11, 2);
        color("minecraft:bedrock", 11, 2);
        color("minecraft:deepslate", 11, 2);
        color("minecraft:stone_bricks", 11, 2);
        color("minecraft:mossy_cobblestone", 11, 2);

        color("minecraft:water", 12, 2);

        color("minecraft:oak_planks", 13, 2);
        color("minecraft:oak_log", 13, 2);
        color("minecraft:birch_planks", 13, 2);
        color("minecraft:birch_log", 13, 2);
        color("minecraft:spruce_planks", 13, 2);
        color("minecraft:spruce_log", 13, 2);
        color("minecraft:jungle_planks", 13, 2);
        color("minecraft:jungle_log", 13, 2);
        color("minecraft:acacia_planks", 13, 2);
        color("minecraft:acacia_log", 13, 2);
        color("minecraft:dark_oak_planks", 13, 2);
        color("minecraft:dark_oak_log", 13, 2);
        color("minecraft:stripped_oak_log", 13, 2);

        color("minecraft:quartz_block", 8, 3);
        color("minecraft:dripstone_block", 10, 1);

        color("minecraft:netherrack", 35, 2);
        color("minecraft:nether_bricks", 35, 1);
        color("minecraft:crimson_nylium", 35, 2);
        color("minecraft:warped_nylium", 56, 2);
        color("minecraft:soul_sand", 42, 2);
        color("minecraft:soul_soil", 42, 2);
        color("minecraft:basalt", 11, 1);
        color("minecraft:blackstone", 11, 0);
        color("minecraft:glowstone", 2, 3);

        color("minecraft:gold_block", 30, 2);
        color("minecraft:diamond_block", 31, 2);
        color("minecraft:lapis_block", 32, 2);
        color("minecraft:emerald_block", 33, 2);

        color("minecraft:obsidian", 29, 2);
        color("minecraft:crying_obsidian", 29, 2);

        color("minecraft:terracotta", 34, 2);
        color("minecraft:red_terracotta", 28, 2);
        color("minecraft:orange_terracotta", 23, 2);
        color("minecraft:yellow_terracotta", 24, 2);
        color("minecraft:brown_terracotta", 26, 2);
        color("minecraft:white_terracotta", 25, 2);

        color("minecraft:mycelium", 27, 2);

        color("minecraft:prismarine", 39, 2);
        color("minecraft:prismarine_bricks", 39, 2);
        color("minecraft:dark_prismarine", 39, 1);

        color("minecraft:moss_block", 7, 1);
        color("minecraft:moss_carpet", 7, 1);

        color("minecraft:cherry_planks", 48, 2);
        color("minecraft:cherry_log", 48, 2);
        color("minecraft:cherry_leaves", 48, 3);

        color("minecraft:copper_block", 50, 2);
        color("minecraft:exposed_copper", 50, 1);
        color("minecraft:weathered_copper", 50, 0);
        color("minecraft:oxidized_copper", 51, 2);

        loadAutoMappingsFromMinestomData();
    }

    private static void color(String block, int baseColor, int shade) {
        BLOCK_COLORS.put(block, (byte) (baseColor * 4 + shade));
    }

    public static byte getMapColor(Block block) {
        String key = block.key().toString();

        Byte manual = BLOCK_COLORS.get(key);
        if (manual != null) {
            return manual;
        }

        Byte autoState = AUTO_STATE_COLORS.get(key + block.state());
        if (autoState != null) {
            return autoState;
        }

        Byte autoDefault = AUTO_DEFAULT_COLORS.get(key);
        if (autoDefault != null) {
            return autoDefault;
        }

        return 0;
    }

    private static void loadAutoMappingsFromMinestomData() {
        try (InputStream input = BlockColorMapping.class.getClassLoader().getResourceAsStream("block.json")) {
            if (input == null) {
                return;
            }

            JsonObject root = JsonParser.parseReader(new InputStreamReader(input, StandardCharsets.UTF_8)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> blockEntry : root.entrySet()) {
                if (!blockEntry.getValue().isJsonObject()) {
                    continue;
                }

                String blockKey = blockEntry.getKey();
                JsonObject blockObject = blockEntry.getValue().getAsJsonObject();
                int defaultMapColorId = getOptionalInt(blockObject, "mapColorId", -1);
                if (defaultMapColorId >= 0) {
                    AUTO_DEFAULT_COLORS.put(blockKey, toMapByte(defaultMapColorId));
                }

                if (!blockObject.has("states") || !blockObject.get("states").isJsonObject()) {
                    continue;
                }

                JsonObject states = blockObject.getAsJsonObject("states");
                for (Map.Entry<String, JsonElement> stateEntry : states.entrySet()) {
                    if (!stateEntry.getValue().isJsonObject()) {
                        continue;
                    }

                    JsonObject stateObject = stateEntry.getValue().getAsJsonObject();
                    int stateMapColorId = getOptionalInt(stateObject, "mapColorId", defaultMapColorId);
                    if (stateMapColorId >= 0) {
                        AUTO_STATE_COLORS.put(blockKey + stateEntry.getKey(), toMapByte(stateMapColorId));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static int getOptionalInt(JsonObject object, String key, int fallback) {
        if (!object.has(key) || !object.get(key).isJsonPrimitive()) {
            return fallback;
        }
        try {
            return object.get(key).getAsInt();
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static byte toMapByte(int mapColorId) {
        return (byte) (mapColorId * 4 + 2);
    }
}
