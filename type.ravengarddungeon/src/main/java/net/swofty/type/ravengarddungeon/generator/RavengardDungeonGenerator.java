package net.swofty.type.ravengarddungeon.generator;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;
import net.swofty.dungeons.ravengard.RavengardDungeonLayout;
import net.swofty.dungeons.ravengard.RavengardDungeonLayout.OpenSocket;
import net.swofty.dungeons.ravengard.RavengardDungeonLayout.Placement;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class RavengardDungeonGenerator {
    private static final Path CATALOG_PATH = Path.of("./configuration/ravengard/dungeon_rooms.json");
    private static final Block PLUG = Block.POLISHED_BLACKSTONE_BRICKS;
    private static final Map<String, Block> ROTATED = new ConcurrentHashMap<>();

    private static RavengardRoomCatalog catalog;

    public record PlacedObject(String category, String type, double x, double y, double z, double yaw) {
    }

    public record GeneratedDungeon(RavengardDungeonLayout layout, List<PlacedObject> objects,
                                   Pos spawn, long seed) {
    }

    public static synchronized RavengardRoomCatalog catalog() {
        if (catalog == null) {
            catalog = RavengardRoomCatalog.load(CATALOG_PATH);
        }
        return catalog;
    }

    public static GeneratedDungeon generate(long seed, int targetRooms) {
        RavengardDungeonLayout layout = RavengardDungeonLayout.generate(catalog(), seed, targetRooms);

        List<PlacedObject> objects = new ArrayList<>();
        for (Placement placement : layout.placements()) {
            for (RavengardRoomCatalog.ObjectSpawn spawn : placement.room().objects()) {
                objects.add(new PlacedObject(spawn.category(), spawn.type(),
                        placement.worldX(spawn.x(), spawn.z()), spawn.y(),
                        placement.worldZ(spawn.x(), spawn.z()),
                        (spawn.yaw() + placement.rotation()) % 360));
            }
        }

        Placement start = layout.start();
        Pos spawn = new Pos(start.originX() + start.footprintWidth() / 2.0, 66,
                start.originZ() + start.footprintDepth() / 2.0);
        return new GeneratedDungeon(layout, objects, spawn, seed);
    }

    public static CompletableFuture<Void> stamp(GeneratedDungeon dungeon, Instance instance) {
        DungeonTemplate template = DungeonTemplate.get();
        int floorY = catalog().floorY(), roofY = catalog().roofY();

        AbsoluteBlockBatch batch = new AbsoluteBlockBatch();
        for (Placement placement : dungeon.layout().placements()) {
            RavengardRoomCatalog.Room room = placement.room();
            int width = placement.footprintWidth(), depth = placement.footprintDepth();
            for (int localX = -1; localX <= width; localX++) {
                for (int localZ = -1; localZ <= depth; localZ++) {
                    int[] source = placement.unrotateLocal(localX, localZ);
                    int sourceX = room.x0() + source[0];
                    int sourceZ = room.z0() + source[1];
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
        for (OpenSocket socket : dungeon.layout().sealedSockets()) {
            plug(batch, socket);
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        batch.apply(instance, applied -> future.complete(null));
        return future;
    }

    private static final String[] CARDINALS = {"north", "east", "south", "west"};

    private static Block rotate(Block block, int rotation) {
        if (rotation == 0) return block;
        Map<String, String> properties = block.properties();
        if (properties.isEmpty()) return block;
        String cacheKey = block.stateId() + ":" + rotation;
        Block cached = ROTATED.get(cacheKey);
        if (cached != null) return cached;

        int steps = rotation / 90;
        Map<String, String> rotated = new HashMap<>(properties);
        String facing = properties.get("facing");
        if (facing != null) {
            int index = cardinalIndex(facing);
            if (index >= 0) rotated.put("facing", CARDINALS[(index + steps) % 4]);
        }
        String axis = properties.get("axis");
        if (axis != null && steps % 2 == 1) {
            if (axis.equals("x")) rotated.put("axis", "z");
            else if (axis.equals("z")) rotated.put("axis", "x");
        }
        String spin = properties.get("rotation");
        if (spin != null) {
            rotated.put("rotation", String.valueOf((Integer.parseInt(spin) + steps * 4) % 16));
        }
        boolean directionalKeys = properties.containsKey("north") || properties.containsKey("east");
        if (directionalKeys) {
            for (int i = 0; i < 4; i++) {
                String value = properties.get(CARDINALS[i]);
                if (value != null) {
                    rotated.put(CARDINALS[(i + steps) % 4], value);
                }
            }
        }
        Block result = block.withProperties(rotated);
        ROTATED.put(cacheKey, result);
        return result;
    }

    private static int cardinalIndex(String side) {
        for (int i = 0; i < 4; i++) {
            if (CARDINALS[i].equals(side)) return i;
        }
        return -1;
    }

    private static void plug(AbsoluteBlockBatch batch, OpenSocket socket) {
        int worldX = socket.worldX();
        int worldZ = socket.worldZ();
        int baseY = (int) Math.floor(socket.y()) - 1;
        String side = socket.worldSide();
        boolean alongX = side.equals("north") || side.equals("south");
        for (int depth = 0; depth <= 1; depth++) {
            int shift = side.equals("east") || side.equals("south") ? depth : -depth;
            for (int spread = -2; spread <= 2; spread++) {
                for (int y = baseY; y <= baseY + 5; y++) {
                    int x = alongX ? worldX + spread : worldX + shift;
                    int z = alongX ? worldZ + shift : worldZ + spread;
                    batch.setBlock(x, y, z, PLUG);
                }
            }
        }
    }
}
