package net.swofty.type.ravengarddungeon.generator;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;
import net.swofty.dungeons.ravengard.Direction;
import net.swofty.dungeons.ravengard.RavengardDungeon;
import net.swofty.dungeons.ravengard.RavengardDungeon.PlacedSocket;
import net.swofty.dungeons.ravengard.RavengardDungeon.RoomPlacement;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog;
import net.swofty.dungeons.ravengard.Rotation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class RavengardDungeonGenerator {
    private static final Path CATALOG_PATH = Path.of("./configuration/ravengard/dungeon_rooms.json");
    private static final Block DOOR_PLUG = Block.POLISHED_BLACKSTONE_BRICKS;
    private static final Map<String, Block> ROTATED_BLOCKS = new ConcurrentHashMap<>();
    private static final String[] CARDINAL_PROPERTIES = {"north", "east", "south", "west"};

    private static RavengardRoomCatalog catalog;

    public record PlacedObject(String category, String type, double x, double y, double z, double yaw) {
    }

    public record GeneratedDungeon(RavengardDungeon dungeon, List<PlacedObject> objects,
                                   Pos spawn, long seed) {
    }

    public static synchronized RavengardRoomCatalog getCatalog() {
        if (catalog == null) {
            catalog = RavengardRoomCatalog.load(CATALOG_PATH);
        }
        return catalog;
    }

    public static GeneratedDungeon generate(long seed, int targetRoomCount) {
        RavengardDungeon dungeon = RavengardDungeon.generate(getCatalog(), seed, targetRoomCount);

        List<PlacedObject> objects = new ArrayList<>();
        for (RoomPlacement placement : dungeon.getPlacements()) {
            for (RavengardRoomCatalog.RoomObject object : placement.room().getObjects()) {
                objects.add(new PlacedObject(object.getCategory(), object.getType(),
                        placement.getWorldX(object.getX(), object.getZ()), object.getY(),
                        placement.getWorldZ(object.getX(), object.getZ()),
                        (object.getYaw() + placement.rotation().getDegrees()) % 360));
            }
        }

        RoomPlacement start = dungeon.getStartRoom();
        Pos spawn = new Pos(start.originX() + start.getFootprintWidth() / 2.0, 66,
                start.originZ() + start.getFootprintDepth() / 2.0);
        return new GeneratedDungeon(dungeon, objects, spawn, seed);
    }

    public static CompletableFuture<Void> stamp(GeneratedDungeon generated, Instance instance) {
        DungeonTemplate template = DungeonTemplate.get();
        int floorY = getCatalog().getFloorY(), roofY = getCatalog().getRoofY();

        AbsoluteBlockBatch batch = new AbsoluteBlockBatch();
        for (RoomPlacement placement : generated.dungeon().getPlacements()) {
            RavengardRoomCatalog.DungeonRoom room = placement.room();
            int footprintWidth = placement.getFootprintWidth();
            int footprintDepth = placement.getFootprintDepth();
            // dilate the footprint mask by one block so the seam half around the
            // room's true shape travels with it, without dragging neighbours along
            boolean[][] cut = new boolean[footprintWidth + 2][footprintDepth + 2];
            placement.forEachMaskCell((localX, localZ) -> {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        int cutX = localX + dx + 1, cutZ = localZ + dz + 1;
                        if (cutX >= 0 && cutX < footprintWidth + 2
                                && cutZ >= 0 && cutZ < footprintDepth + 2) {
                            cut[cutX][cutZ] = true;
                        }
                    }
                }
            });
            for (int localX = -1; localX <= footprintWidth; localX++) {
                for (int localZ = -1; localZ <= footprintDepth; localZ++) {
                    if (!cut[localX + 1][localZ + 1]) continue;
                    int[] source = placement.toSourceFrame(localX, localZ);
                    int sourceX = room.getMinX() + source[0];
                    int sourceZ = room.getMinZ() + source[1];
                    int targetX = placement.originX() + localX;
                    int targetZ = placement.originZ() + localZ;
                    for (int y = floorY; y <= roofY; y++) {
                        Block block = template.blockAt(sourceX, y, sourceZ);
                        if (block != Block.AIR) {
                            batch.setBlock(targetX, y, targetZ, rotate(block, placement.rotation()));
                        }
                    }
                }
            }
        }
        for (PlacedSocket socket : generated.dungeon().getSealedSockets()) {
            plugDoorway(batch, socket);
        }

        // batches only land in loaded chunks, and a fresh instance has none
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        for (RoomPlacement placement : generated.dungeon().getPlacements()) {
            minX = Math.min(minX, placement.originX() - 2);
            maxX = Math.max(maxX, placement.originX() + placement.getFootprintWidth() + 2);
            minZ = Math.min(minZ, placement.originZ() - 2);
            maxZ = Math.max(maxZ, placement.originZ() + placement.getFootprintDepth() + 2);
        }
        List<CompletableFuture<?>> chunkLoads = new ArrayList<>();
        for (int chunkX = minX >> 4; chunkX <= (maxX >> 4); chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= (maxZ >> 4); chunkZ++) {
                chunkLoads.add(instance.loadChunk(chunkX, chunkZ));
            }
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.allOf(chunkLoads.toArray(new CompletableFuture[0]))
                .thenRun(() -> batch.apply(instance, applied ->
                        // lazy lighting on a fresh two thousand chunk layout floods the
                        // executor and the client renders unlit sections as invisible,
                        // so lighting is part of readiness
                        Thread.startVirtualThread(() -> {
                            long lightingStarted = System.currentTimeMillis();
                            try {
                                net.minestom.server.instance.LightingChunk.relight(instance,
                                        new ArrayList<>(instance.getChunks()));
                                org.tinylog.Logger.info("Dungeon relight of {} chunks took {}ms",
                                        instance.getChunks().size(),
                                        System.currentTimeMillis() - lightingStarted);
                            } catch (Exception exception) {
                                org.tinylog.Logger.error(exception,
                                        "Dungeon relight failed after {}ms",
                                        System.currentTimeMillis() - lightingStarted);
                            }
                            future.complete(null);
                        })))
                .exceptionally(throwable -> {
                    future.completeExceptionally(throwable);
                    return null;
                });
        return future;
    }

    private static Block rotate(Block block, Rotation rotation) {
        if (rotation == Rotation.NONE) return block;
        Map<String, String> properties = block.properties();
        if (properties.isEmpty()) return block;
        String cacheKey = block.stateId() + ":" + rotation;
        Block cached = ROTATED_BLOCKS.get(cacheKey);
        if (cached != null) return cached;

        int quarterTurns = rotation.getQuarterTurns();
        Map<String, String> rotated = new HashMap<>(properties);
        String facing = properties.get("facing");
        if (facing != null) {
            int index = cardinalIndex(facing);
            if (index >= 0) rotated.put("facing", CARDINAL_PROPERTIES[(index + quarterTurns) % 4]);
        }
        String axis = properties.get("axis");
        if (axis != null && rotation.swapsAxes()) {
            if (axis.equals("x")) rotated.put("axis", "z");
            else if (axis.equals("z")) rotated.put("axis", "x");
        }
        String standingRotation = properties.get("rotation");
        if (standingRotation != null) {
            rotated.put("rotation",
                    String.valueOf((Integer.parseInt(standingRotation) + quarterTurns * 4) % 16));
        }
        if (properties.containsKey("north") || properties.containsKey("east")) {
            for (int i = 0; i < 4; i++) {
                String value = properties.get(CARDINAL_PROPERTIES[i]);
                if (value != null) {
                    rotated.put(CARDINAL_PROPERTIES[(i + quarterTurns) % 4], value);
                }
            }
        }
        Block result = block.withProperties(rotated);
        ROTATED_BLOCKS.put(cacheKey, result);
        return result;
    }

    private static int cardinalIndex(String side) {
        for (int i = 0; i < 4; i++) {
            if (CARDINAL_PROPERTIES[i].equals(side)) return i;
        }
        return -1;
    }

    private static void plugDoorway(AbsoluteBlockBatch batch, PlacedSocket socket) {
        int worldX = socket.getWorldX();
        int worldZ = socket.getWorldZ();
        int baseY = (int) Math.floor(socket.getY()) - 1;
        Direction side = socket.getWorldSide();
        boolean alongX = side == Direction.NORTH || side == Direction.SOUTH;
        for (int depth = 0; depth <= 1; depth++) {
            int shift = side == Direction.EAST || side == Direction.SOUTH ? depth : -depth;
            for (int spread = -2; spread <= 2; spread++) {
                for (int y = baseY; y <= baseY + 5; y++) {
                    int x = alongX ? worldX + spread : worldX + shift;
                    int z = alongX ? worldZ + shift : worldZ + spread;
                    batch.setBlock(x, y, z, DOOR_PLUG);
                }
            }
        }
    }
}
