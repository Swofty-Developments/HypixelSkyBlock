package net.swofty.type.skyblockgeneric.user.island;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.hollowcube.polar.PolarLoader;
import net.hollowcube.polar.PolarReader;
import net.hollowcube.polar.PolarWorld;
import net.hollowcube.polar.PolarWriter;
import net.swofty.commons.CustomWorlds;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.data.monogdb.IslandDatabase;
import org.bson.types.Binary;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Path;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IslandWorldStorage {
    private static final Path TEMPLATE_PATH = CustomWorlds.SKYBLOCK_ISLAND_TEMPLATE.getPath();

    public static LoadedIslandWorld load(IslandDatabase database) {
        if (!database.exists()) {
            return new LoadedIslandWorld(templateWorld(), HypixelConst.getCurrentIslandVersion(), 0, true);
        }

        int version = database.has("version") ? (int) database.get("version", Integer.class) : 0;

        if (version == 0) {
            return new LoadedIslandWorld(templateWorld(), version, System.currentTimeMillis(), false);
        }

        PolarWorld world = PolarReader.read(((Binary) database.get("data", Binary.class)).getData());
        long lastSaved = (long) database.get("lastSaved", Long.class);
        return new LoadedIslandWorld(world, version, lastSaved, false);
    }

    public static void save(IslandDatabase database, PolarWorld world, int version) {
        database.insertOrUpdate("data", new Binary(PolarWriter.write(world)));
        database.insertOrUpdate("lastSaved", System.currentTimeMillis());
        database.insertOrUpdate("version", version);
    }

    private static PolarWorld templateWorld() {
        try {
            return new PolarLoader(TEMPLATE_PATH).world();
        } catch (IOException e) {
            Logger.error("Failed to create island world", e);
            throw new RuntimeException("Failed to create island world", e);
        }
    }

    public record LoadedIslandWorld(PolarWorld world, int version, long lastSaved, boolean firstCreated) {
    }
}
