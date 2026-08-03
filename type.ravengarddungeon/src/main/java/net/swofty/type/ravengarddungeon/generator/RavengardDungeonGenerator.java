package net.swofty.type.ravengarddungeon.generator;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.AbsoluteBlockBatch;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class RavengardDungeonGenerator {
    private static final int SEAM = 2;
    private static final Block PLUG = Block.POLISHED_BLACKSTONE_BRICKS;

    public record Placement(DungeonRoomCatalog.Room room, int originX, int originZ) {
        public int worldX(double localX) {
            return originX + (int) Math.round(localX);
        }

        public int worldZ(double localZ) {
            return originZ + (int) Math.round(localZ);
        }
    }

    public record PlacedObject(String category, String type, double x, double y, double z, double yaw) {
    }

    public record GeneratedDungeon(List<Placement> placements, List<PlacedObject> objects,
                                   List<OpenSocket> sealedSockets, Pos spawn, long seed) {
    }

    public static GeneratedDungeon generate(long seed, int targetRooms) {
        DungeonRoomCatalog catalog = DungeonRoomCatalog.get();
        Random random = new Random(seed);
        List<DungeonRoomCatalog.Room> pool = new ArrayList<>(catalog.rooms());

        List<DungeonRoomCatalog.Room> hubs = pool.stream()
                .filter(room -> room.sockets().size() >= 3).toList();
        DungeonRoomCatalog.Room start = hubs.get(random.nextInt(hubs.size()));

        List<Placement> placements = new ArrayList<>();
        Set<Long> occupied = new HashSet<>();
        List<OpenSocket> open = new ArrayList<>();
        Set<String> usedRooms = new HashSet<>();

        place(start, -start.width() / 2, -start.depth() / 2, placements, occupied, open, usedRooms);

        List<OpenSocket> sealed = new ArrayList<>();
        while (placements.size() < targetRooms && !open.isEmpty()) {
            OpenSocket socket = open.remove(random.nextInt(open.size()));
            List<Candidate> candidates = new ArrayList<>();
            for (DungeonRoomCatalog.Room room : pool) {
                if (usedRooms.contains(room.id()) && usedRooms.size() < pool.size() / 2) continue;
                for (DungeonRoomCatalog.Socket other : room.sockets()) {
                    if (!other.side().equals(socket.socket.opposite())) continue;
                    if (Math.abs(other.y() - socket.socket.y()) > 1.5) continue;
                    int[] origin = alignedOrigin(socket, room, other);
                    if (fits(room, origin[0], origin[1], occupied)) {
                        candidates.add(new Candidate(room, other, origin[0], origin[1]));
                    }
                }
            }
            if (candidates.isEmpty()) {
                sealed.add(socket);
                continue;
            }
            Candidate chosen = candidates.get(random.nextInt(candidates.size()));
            place(chosen.room, chosen.originX, chosen.originZ, placements, occupied, open, usedRooms);
            open.removeIf(o -> o.placement.room().id().equals(chosen.room.id())
                    && o.placement.originX == chosen.originX && o.placement.originZ == chosen.originZ
                    && o.socket == chosen.socket);
        }
        sealed.addAll(open);

        List<PlacedObject> objects = new ArrayList<>();
        for (Placement placement : placements) {
            for (DungeonRoomCatalog.ObjectSpawn spawn : placement.room().objects()) {
                objects.add(new PlacedObject(spawn.category(), spawn.type(),
                        placement.originX() + spawn.x(), spawn.y(),
                        placement.originZ() + spawn.z(), spawn.yaw()));
            }
        }

        Placement first = placements.getFirst();
        Pos spawn = new Pos(first.originX() + first.room().width() / 2.0, 66,
                first.originZ() + first.room().depth() / 2.0);
        return new GeneratedDungeon(placements, objects, sealed, spawn, seed);
    }

    private static void place(DungeonRoomCatalog.Room room, int originX, int originZ,
                              List<Placement> placements, Set<Long> occupied,
                              List<OpenSocket> open, Set<String> usedRooms) {
        Placement placement = new Placement(room, originX, originZ);
        placements.add(placement);
        usedRooms.add(room.id());
        for (int x = originX - 1; x <= originX + room.width(); x += 2) {
            for (int z = originZ - 1; z <= originZ + room.depth(); z += 2) {
                occupied.add(cell(x, z));
            }
        }
        for (DungeonRoomCatalog.Socket socket : room.sockets()) {
            open.add(new OpenSocket(placement, socket));
        }
    }

    private static int[] alignedOrigin(OpenSocket from, DungeonRoomCatalog.Room room,
                                       DungeonRoomCatalog.Socket socket) {
        int worldX = from.placement.worldX(from.socket.x());
        int worldZ = from.placement.worldZ(from.socket.z());
        return switch (from.socket.side()) {
            case "east" -> new int[]{from.placement.originX() + from.placement.room().width() - 1 + SEAM,
                    worldZ - (int) Math.round(socket.z())};
            case "west" -> new int[]{from.placement.originX() - SEAM - room.width() + 1,
                    worldZ - (int) Math.round(socket.z())};
            case "south" -> new int[]{worldX - (int) Math.round(socket.x()),
                    from.placement.originZ() + from.placement.room().depth() - 1 + SEAM};
            default -> new int[]{worldX - (int) Math.round(socket.x()),
                    from.placement.originZ() - SEAM - room.depth() + 1};
        };
    }

    private static boolean fits(DungeonRoomCatalog.Room room, int originX, int originZ,
                                Set<Long> occupied) {
        for (int x = originX; x < originX + room.width(); x += 2) {
            for (int z = originZ; z < originZ + room.depth(); z += 2) {
                if (occupied.contains(cell(x, z))) return false;
            }
        }
        return true;
    }

    private static long cell(int x, int z) {
        return ((long) (x >> 1) << 32) | ((z >> 1) & 0xFFFFFFFFL);
    }

    public static CompletableFuture<Void> stamp(GeneratedDungeon dungeon, Instance instance) {
        DungeonTemplate template = DungeonTemplate.get();
        DungeonRoomCatalog catalog = DungeonRoomCatalog.get();
        int floorY = catalog.floorY(), roofY = catalog.roofY();

        AbsoluteBlockBatch batch = new AbsoluteBlockBatch();
        for (Placement placement : dungeon.placements()) {
            DungeonRoomCatalog.Room room = placement.room();
            for (int localX = -1; localX <= room.width(); localX++) {
                for (int localZ = -1; localZ <= room.depth(); localZ++) {
                    int sourceX = room.x0() + localX;
                    int sourceZ = room.z0() + localZ;
                    int targetX = placement.originX() + localX;
                    int targetZ = placement.originZ() + localZ;
                    for (int y = floorY; y <= roofY; y++) {
                        Block block = template.blockAt(sourceX, y, sourceZ);
                        if (block != Block.AIR) {
                            batch.setBlock(targetX, y, targetZ, block);
                        }
                    }
                }
            }
        }
        for (OpenSocket socket : dungeon.sealedSockets()) {
            plug(batch, socket);
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        batch.apply(instance, applied -> future.complete(null));
        return future;
    }

    private static void plug(AbsoluteBlockBatch batch, OpenSocket socket) {
        int worldX = socket.placement.worldX(socket.socket.x());
        int worldZ = socket.placement.worldZ(socket.socket.z());
        int baseY = (int) Math.floor(socket.socket.y()) - 1;
        boolean alongX = socket.socket.side().equals("north") || socket.socket.side().equals("south");
        for (int depth = 0; depth <= 1; depth++) {
            int shift = socket.socket.side().equals("east") || socket.socket.side().equals("south")
                    ? depth : -depth;
            for (int spread = -2; spread <= 2; spread++) {
                for (int y = baseY; y <= baseY + 5; y++) {
                    int x = alongX ? worldX + spread : worldX + shift;
                    int z = alongX ? worldZ + shift : worldZ + spread;
                    batch.setBlock(x, y, z, PLUG);
                }
            }
        }
    }

    public static final class OpenSocket {
        final Placement placement;
        final DungeonRoomCatalog.Socket socket;

        OpenSocket(Placement placement, DungeonRoomCatalog.Socket socket) {
            this.placement = placement;
            this.socket = socket;
        }
    }

    public record Candidate(DungeonRoomCatalog.Room room, DungeonRoomCatalog.Socket socket,
                            int originX, int originZ) {
    }
}
