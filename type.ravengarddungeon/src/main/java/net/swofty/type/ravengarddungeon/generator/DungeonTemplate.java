package net.swofty.type.ravengarddungeon.generator;

import net.hollowcube.polar.PolarChunk;
import net.hollowcube.polar.PolarReader;
import net.hollowcube.polar.PolarSection;
import net.hollowcube.polar.PolarWorld;
import net.minestom.server.instance.block.Block;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DungeonTemplate {
    private static DungeonTemplate instance;

    private final PolarWorld world;
    private final Map<String, Block> parsed = new ConcurrentHashMap<>();

    private DungeonTemplate(PolarWorld world) {
        this.world = world;
    }

    public static DungeonTemplate get() {
        if (instance == null) {
            Path path = Path.of("./configuration/ravengard/dungeon_1.polar");
            try {
                instance = new DungeonTemplate(PolarReader.read(Files.readAllBytes(path)));
            } catch (Exception exception) {
                throw new IllegalStateException("Could not load dungeon template " + path, exception);
            }
        }
        return instance;
    }

    public Block blockAt(int x, int y, int z) {
        PolarChunk chunk = world.chunkAt(x >> 4, z >> 4);
        if (chunk == null) return Block.AIR;
        int sectionIndex = (y >> 4) - world.minSection();
        PolarSection[] sections = chunk.sections();
        if (sectionIndex < 0 || sectionIndex >= sections.length) return Block.AIR;
        PolarSection section = sections[sectionIndex];
        if (section.isEmpty()) return Block.AIR;
        String[] palette = section.blockPalette();
        int paletteIndex = 0;
        int[] data = section.blockData();
        if (data != null && palette.length > 1) {
            paletteIndex = data[((y & 15) << 8) | ((z & 15) << 4) | (x & 15)];
        }
        String state = palette[paletteIndex];
        if (state.endsWith("air")) return Block.AIR;
        return parse(state);
    }

    private Block parse(String state) {
        return parsed.computeIfAbsent(state, key -> {
            String name = key;
            Map<String, String> properties = new HashMap<>();
            int bracket = key.indexOf('[');
            if (bracket >= 0) {
                name = key.substring(0, bracket);
                String inner = key.substring(bracket + 1, key.length() - 1);
                for (String pair : inner.split(",")) {
                    int eq = pair.indexOf('=');
                    if (eq > 0) {
                        properties.put(pair.substring(0, eq).trim(), pair.substring(eq + 1).trim());
                    }
                }
            }
            Block block = Block.fromKey(name);
            if (block == null) return Block.AIR;
            return properties.isEmpty() ? block : block.withProperties(properties);
        });
    }
}
