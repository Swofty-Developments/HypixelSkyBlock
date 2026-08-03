package net.swofty.dungeons.ravengard;

import net.swofty.dungeons.ravengard.RavengardRoomCatalog.Room;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog.Socket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class RavengardDungeonLayout {
    public static final int SEAM = 2;
    private static final String[] SIDES = {"north", "east", "south", "west"};

    private final List<Placement> placements = new ArrayList<>();
    private final List<OpenSocket> sealed = new ArrayList<>();
    private final long seed;

    private RavengardDungeonLayout(long seed) {
        this.seed = seed;
    }

    public List<Placement> placements() {
        return placements;
    }

    public List<OpenSocket> sealedSockets() {
        return sealed;
    }

    public long seed() {
        return seed;
    }

    public Placement start() {
        return placements.getFirst();
    }

    public static String rotateSide(String side, int rotation) {
        int index = 0;
        for (int i = 0; i < SIDES.length; i++) {
            if (SIDES[i].equals(side)) index = i;
        }
        return SIDES[(index + rotation / 90) % 4];
    }

    public record Placement(Room room, int rotation, int originX, int originZ) {
        public int footprintWidth() {
            return rotation % 180 == 0 ? room.width() : room.depth();
        }

        public int footprintDepth() {
            return rotation % 180 == 0 ? room.depth() : room.width();
        }

        /** Rotates room-local source coordinates into the placement's local frame. */
        public double[] rotateLocal(double x, double z) {
            int w = room.width(), d = room.depth();
            return switch (rotation) {
                case 90 -> new double[]{d - 1 - z, x};
                case 180 -> new double[]{w - 1 - x, d - 1 - z};
                case 270 -> new double[]{z, w - 1 - x};
                default -> new double[]{x, z};
            };
        }

        /** Inverse: placement-local coordinates back to room-local source coordinates. */
        public int[] unrotateLocal(int x, int z) {
            int w = room.width(), d = room.depth();
            return switch (rotation) {
                case 90 -> new int[]{z, d - 1 - x};
                case 180 -> new int[]{w - 1 - x, d - 1 - z};
                case 270 -> new int[]{w - 1 - z, x};
                default -> new int[]{x, z};
            };
        }

        public double worldX(double localSourceX, double localSourceZ) {
            return originX + rotateLocal(localSourceX, localSourceZ)[0];
        }

        public double worldZ(double localSourceX, double localSourceZ) {
            return originZ + rotateLocal(localSourceX, localSourceZ)[1];
        }
    }

    public record OpenSocket(Placement placement, Socket socket) {
        public String worldSide() {
            return rotateSide(socket.side(), placement.rotation());
        }

        public int worldX() {
            return (int) Math.round(placement.worldX(socket.x(), socket.z()));
        }

        public int worldZ() {
            return (int) Math.round(placement.worldZ(socket.x(), socket.z()));
        }

        public double y() {
            return socket.y();
        }
    }

    public static RavengardDungeonLayout generate(RavengardRoomCatalog catalog, long seed, int targetRooms) {
        RavengardDungeonLayout layout = new RavengardDungeonLayout(seed);
        Random random = new Random(seed);
        List<Room> pool = catalog.rooms();

        List<Room> hubs = pool.stream().filter(room -> room.sockets().size() >= 3).toList();
        Room start = hubs.get(random.nextInt(hubs.size()));

        Set<Long> occupied = new HashSet<>();
        List<OpenSocket> open = new ArrayList<>();
        Set<String> used = new HashSet<>();

        layout.place(new Placement(start, 0, -start.width() / 2, -start.depth() / 2),
                occupied, open, used);

        while (layout.placements.size() < targetRooms && !open.isEmpty()) {
            OpenSocket from = open.remove(random.nextInt(open.size()));
            String needed = Socket.oppositeOf(from.worldSide());

            List<Placement> candidates = new ArrayList<>();
            List<Socket> candidateSockets = new ArrayList<>();
            for (Room room : pool) {
                if (used.contains(room.id()) && used.size() < pool.size() / 2) continue;
                for (Socket socket : room.sockets()) {
                    if (Math.abs(socket.y() - from.y()) > 1.5) continue;
                    for (int rotation = 0; rotation < 360; rotation += 90) {
                        if (!rotateSide(socket.side(), rotation).equals(needed)) continue;
                        Placement candidate = aligned(from, room, socket, rotation);
                        if (fits(candidate, occupied)) {
                            candidates.add(candidate);
                            candidateSockets.add(socket);
                        }
                    }
                }
            }
            if (candidates.isEmpty()) {
                layout.sealed.add(from);
                continue;
            }
            int pick = random.nextInt(candidates.size());
            Placement chosen = candidates.get(pick);
            Socket entrySocket = candidateSockets.get(pick);
            layout.place(chosen, occupied, open, used);
            open.removeIf(o -> o.placement() == chosen && o.socket() == entrySocket);
        }
        layout.sealed.addAll(open);
        return layout;
    }

    private void place(Placement placement, Set<Long> occupied, List<OpenSocket> open, Set<String> used) {
        placements.add(placement);
        used.add(placement.room().id());
        for (int x = placement.originX() - 1; x <= placement.originX() + placement.footprintWidth(); x += 2) {
            for (int z = placement.originZ() - 1; z <= placement.originZ() + placement.footprintDepth(); z += 2) {
                occupied.add(cell(x, z));
            }
        }
        for (Socket socket : placement.room().sockets()) {
            open.add(new OpenSocket(placement, socket));
        }
    }

    private static Placement aligned(OpenSocket from, Room room, Socket socket, int rotation) {
        Placement probe = new Placement(room, rotation, 0, 0);
        double[] local = probe.rotateLocal(socket.x(), socket.z());
        int width = probe.footprintWidth(), depth = probe.footprintDepth();
        int worldX = from.worldX(), worldZ = from.worldZ();
        return switch (from.worldSide()) {
            case "east" -> new Placement(room, rotation,
                    from.placement().originX() + from.placement().footprintWidth() - 1 + SEAM,
                    worldZ - (int) Math.round(local[1]));
            case "west" -> new Placement(room, rotation,
                    from.placement().originX() - SEAM - width + 1,
                    worldZ - (int) Math.round(local[1]));
            case "south" -> new Placement(room, rotation,
                    worldX - (int) Math.round(local[0]),
                    from.placement().originZ() + from.placement().footprintDepth() - 1 + SEAM);
            default -> new Placement(room, rotation,
                    worldX - (int) Math.round(local[0]),
                    from.placement().originZ() - SEAM - depth + 1);
        };
    }

    private static boolean fits(Placement placement, Set<Long> occupied) {
        for (int x = placement.originX(); x < placement.originX() + placement.footprintWidth(); x += 2) {
            for (int z = placement.originZ(); z < placement.originZ() + placement.footprintDepth(); z += 2) {
                if (occupied.contains(cell(x, z))) return false;
            }
        }
        return true;
    }

    private static long cell(int x, int z) {
        return ((long) (x >> 1) << 32) | ((z >> 1) & 0xFFFFFFFFL);
    }

    /** Plan view, one character per four blocks, in the style of the SkyBlock printout. */
    @Override
    public String toString() {
        final int scale = 4;
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        for (Placement placement : placements) {
            minX = Math.min(minX, placement.originX());
            maxX = Math.max(maxX, placement.originX() + placement.footprintWidth());
            minZ = Math.min(minZ, placement.originZ());
            maxZ = Math.max(maxZ, placement.originZ() + placement.footprintDepth());
        }
        int cols = (maxX - minX) / scale + 2, rows = (maxZ - minZ) / scale + 2;
        char[][] canvas = new char[rows][cols];
        for (char[] row : canvas) java.util.Arrays.fill(row, ' ');

        for (int index = 0; index < placements.size(); index++) {
            Placement placement = placements.get(index);
            char label = index == 0 ? 'S' : Character.toUpperCase(placement.room().color().charAt(0));
            int c0 = (placement.originX() - minX) / scale;
            int c1 = (placement.originX() + placement.footprintWidth() - 1 - minX) / scale;
            int r0 = (placement.originZ() - minZ) / scale;
            int r1 = (placement.originZ() + placement.footprintDepth() - 1 - minZ) / scale;
            for (int c = c0; c <= c1; c++) {
                canvas[r0][c] = '-';
                canvas[r1][c] = '-';
            }
            for (int r = r0; r <= r1; r++) {
                canvas[r][c0] = '|';
                canvas[r][c1] = '|';
            }
            canvas[(r0 + r1) / 2][(c0 + c1) / 2] = label;
        }
        for (Placement placement : placements) {
            for (Socket socket : placement.room().sockets()) {
                OpenSocket world = new OpenSocket(placement, socket);
                int c = (world.worldX() - minX) / scale, r = (world.worldZ() - minZ) / scale;
                if (r >= 0 && r < rows && c >= 0 && c < cols) canvas[r][c] = '+';
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("seed=").append(seed).append(" rooms=").append(placements.size())
                .append(" sealed=").append(sealed.size())
                .append(" bounds=").append(maxX - minX).append('x').append(maxZ - minZ).append('\n');
        for (char[] row : canvas) {
            builder.append(new String(row)).append('\n');
        }
        return builder.toString();
    }
}
