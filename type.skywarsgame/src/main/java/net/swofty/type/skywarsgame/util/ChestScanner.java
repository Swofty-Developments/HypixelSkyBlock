package net.swofty.type.skywarsgame.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.skywars.map.SkywarsMapsConfig;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChestScanner {

    private static final double ISLAND_RADIUS = 15.0;

    private static final int SCAN_HEIGHT = 100;

    public static ChestScanResult scanForChests(
            Instance instance,
            SkywarsMapsConfig.MapBounds bounds,
            List<Pos> islandSpawns,
            int voidY) {

        List<Pos> islandChests = new ArrayList<>();
        List<Pos> centerChests = new ArrayList<>();

        if (bounds == null || bounds.min() == null || bounds.max() == null) {
            Logger.warn("ChestScanner: No bounds defined, skipping scan");
            return new ChestScanResult(islandChests, centerChests);
        }

        ensureChunksLoaded(instance, bounds);

        int minX = (int) Math.min(bounds.min().x(), bounds.max().x());
        int maxX = (int) Math.max(bounds.min().x(), bounds.max().x());
        int minY = voidY;
        int maxY = voidY + SCAN_HEIGHT;
        int minZ = (int) Math.min(bounds.min().z(), bounds.max().z());
        int maxZ = (int) Math.max(bounds.min().z(), bounds.max().z());

        Logger.info("ChestScanner: Scanning area X[{},{}] Y[{},{}] Z[{},{}]",
                minX, maxX, minY, maxY, minZ, maxZ);

        int chestsFound = 0;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = instance.getBlock(x, y, z);

                    if (block.compare(Block.CHEST) || block.compare(Block.TRAPPED_CHEST)) {
                        Pos chestPos = new Pos(x, y, z);
                        chestsFound++;

                        if (isIslandChest(chestPos, islandSpawns)) {
                            islandChests.add(chestPos);
                        } else {
                            centerChests.add(chestPos);
                        }
                    }
                }
            }
        }

        Logger.info("ChestScanner: Found {} chests ({} island, {} center)",
                chestsFound, islandChests.size(), centerChests.size());

        return new ChestScanResult(islandChests, centerChests);
    }

    private static boolean isIslandChest(Pos chestPos, List<Pos> islandSpawns) {
        for (Pos island : islandSpawns) {
            double distance = chestPos.distance(island);
            if (distance <= ISLAND_RADIUS) {
                return true;
            }
        }
        return false;
    }

    private static void ensureChunksLoaded(Instance instance, SkywarsMapsConfig.MapBounds bounds) {
        int minChunkX = (int) Math.min(bounds.min().x(), bounds.max().x()) >> 4;
        int maxChunkX = (int) Math.max(bounds.min().x(), bounds.max().x()) >> 4;
        int minChunkZ = (int) Math.min(bounds.min().z(), bounds.max().z()) >> 4;
        int maxChunkZ = (int) Math.max(bounds.min().z(), bounds.max().z()) >> 4;

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = instance.getChunk(cx, cz);
                if (chunk == null) {
                    futures.add(instance.loadChunk(cx, cz));
                }
            }
        }

        if (!futures.isEmpty()) {
            Logger.debug("ChestScanner: Loading {} chunks", futures.size());
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    public record ChestScanResult(List<Pos> islandChests, List<Pos> centerChests) {}
}
