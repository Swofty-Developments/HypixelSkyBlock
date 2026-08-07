package net.swofty.dungeons.ravengard;

import lombok.Getter;
import net.swofty.dungeons.GameDungeon;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog.DoorSocket;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog.DungeonRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Getter
public final class RavengardDungeon extends GameDungeon {
    public static final int DOOR_SEAM_WIDTH = 2;

    private final List<RoomPlacement> placements = new ArrayList<>();
    private final List<PlacedSocket> sealedSockets = new ArrayList<>();
    private final long seed;
    private int layoutRadius = 320;

    private RavengardDungeon(long seed) {
        this.seed = seed;
    }

    @Override
    public int getRoomCount() {
        return placements.size();
    }

    public RoomPlacement getStartRoom() {
        return placements.getFirst();
    }

    public record RoomPlacement(DungeonRoom room, Rotation rotation, int originX, int originZ) {
        public int getFootprintWidth() {
            return rotation.swapsAxes() ? room.getDepth() : room.getWidth();
        }

        public int getFootprintDepth() {
            return rotation.swapsAxes() ? room.getWidth() : room.getDepth();
        }

        /** Rotates room-local source coordinates into this placement's local frame. */
        public double[] toPlacementFrame(double sourceX, double sourceZ) {
            int width = room.getWidth(), depth = room.getDepth();
            return switch (rotation) {
                case CLOCKWISE_90 -> new double[]{depth - 1 - sourceZ, sourceX};
                case CLOCKWISE_180 -> new double[]{width - 1 - sourceX, depth - 1 - sourceZ};
                case CLOCKWISE_270 -> new double[]{sourceZ, width - 1 - sourceX};
                case NONE -> new double[]{sourceX, sourceZ};
            };
        }

        /** Inverse of {@link #toPlacementFrame}: placement-local back to room-local source. */
        public int[] toSourceFrame(int placementX, int placementZ) {
            int width = room.getWidth(), depth = room.getDepth();
            return switch (rotation) {
                case CLOCKWISE_90 -> new int[]{placementZ, depth - 1 - placementX};
                case CLOCKWISE_180 -> new int[]{width - 1 - placementX, depth - 1 - placementZ};
                case CLOCKWISE_270 -> new int[]{width - 1 - placementZ, placementX};
                case NONE -> new int[]{placementX, placementZ};
            };
        }

        /** Visits every mask cell of this placement in placement-local coordinates. */
        public void forEachMaskCell(java.util.function.BiConsumer<Integer, Integer> visitor) {
            for (int[] run : room.getMask()) {
                for (int sourceX = run[1]; sourceX <= run[2]; sourceX++) {
                    double[] local = toPlacementFrame(sourceX, run[0]);
                    visitor.accept((int) Math.round(local[0]), (int) Math.round(local[1]));
                }
            }
        }

        public double getWorldX(double sourceX, double sourceZ) {
            return originX + toPlacementFrame(sourceX, sourceZ)[0];
        }

        public double getWorldZ(double sourceX, double sourceZ) {
            return originZ + toPlacementFrame(sourceX, sourceZ)[1];
        }
    }

    public record PlacedSocket(RoomPlacement placement, DoorSocket socket) {
        public Direction getWorldSide() {
            return socket.getSide().rotated(placement.rotation());
        }

        public int getWorldX() {
            return (int) Math.round(placement.getWorldX(socket.getX(), socket.getZ()));
        }

        public int getWorldZ() {
            return (int) Math.round(placement.getWorldZ(socket.getX(), socket.getZ()));
        }

        public double getY() {
            return socket.getY();
        }
    }

    private static int layoutRadiusFor(RavengardRoomCatalog catalog, int targetRoomCount) {
        long totalArea = 0;
        for (DungeonRoom room : catalog.getRooms()) {
            for (int[] run : room.getMask()) {
                totalArea += run[2] - run[1] + 1;
            }
        }
        double averageRoomArea = totalArea / (double) Math.max(1, catalog.getRooms().size());
        double neededArea = targetRoomCount * averageRoomArea * 1.6;
        int radius = (int) Math.ceil(Math.sqrt(neededArea) / 2) + 20;
        return Math.clamp(radius, 90, 320);
    }

    /** Slot swaps chosen for this seed: the placement sits at its slot's
     * original city position with a different room's interior stamped in. */
    private final List<RoomPlacement> swaps = new ArrayList<>();

    public List<RoomPlacement> getSwaps() {
        return swaps;
    }

    /**
     * The blueprint is a fixed city; outlined rooms are swappable slots cut into
     * it. Generation keeps every slot at its original position and permutes
     * rooms between slots whose footprints match under rotation.
     */
    public static RavengardDungeon generate(RavengardRoomCatalog catalog, long seed, int targetRoomCount) {
        RavengardDungeon dungeon = new RavengardDungeon(seed);
        Random random = new Random(seed);
        List<DungeonRoom> rooms = catalog.getRooms();

        DungeonRoom citadel = catalog.getCitadel();
        if (citadel != null) {
            dungeon.placements.add(new RoomPlacement(citadel, Rotation.NONE,
                    citadel.getMinX(), citadel.getMinZ()));
        }

        Map<String, List<DungeonRoom>> shapeGroups = new HashMap<>();
        for (DungeonRoom room : rooms) {
            // the outline colour is the room's role class in the blueprint;
            // interiors only permute between rooms of the same class
            shapeGroups.computeIfAbsent(room.getColor() + "|" + canonicalShape(room),
                    key -> new ArrayList<>()).add(room);
        }

        Map<String, RoomPlacement> chosen = new HashMap<>();
        for (List<DungeonRoom> group : shapeGroups.values()) {
            List<DungeonRoom> shuffled = new ArrayList<>(group);
            Collections.shuffle(shuffled, random);
            for (int index = 0; index < group.size(); index++) {
                DungeonRoom slot = group.get(index);
                DungeonRoom pick = shuffled.get(index);
                Rotation rotation = rotationBetween(pick, slot);
                if (rotation == null) {
                    pick = slot;
                    rotation = Rotation.NONE;
                }
                RoomPlacement placement = new RoomPlacement(pick, rotation,
                        slot.getMinX(), slot.getMinZ());
                chosen.put(slot.getId(), placement);
                if (pick != slot) {
                    dungeon.swaps.add(placement);
                }
            }
        }
        for (DungeonRoom slot : rooms) {
            dungeon.placements.add(chosen.get(slot.getId()));
        }
        return dungeon;
    }

    private static String canonicalShape(DungeonRoom room) {
        String best = null;
        for (Rotation rotation : Rotation.values()) {
            String shape = shapeKey(room, rotation);
            if (best == null || shape.compareTo(best) < 0) {
                best = shape;
            }
        }
        return best;
    }

    private static String shapeKey(DungeonRoom room, Rotation rotation) {
        List<long[]> cells = new ArrayList<>();
        RoomPlacement frame = new RoomPlacement(room, rotation, 0, 0);
        frame.forEachMaskCell((x, z) -> cells.add(new long[]{x, z}));
        cells.sort((a, b) -> a[0] != b[0] ? Long.compare(a[0], b[0]) : Long.compare(a[1], b[1]));
        StringBuilder key = new StringBuilder();
        for (long[] cell : cells) {
            key.append(cell[0]).append(',').append(cell[1]).append(';');
        }
        return key.toString();
    }

    /** Rotation that maps {@code pick}'s mask exactly onto {@code slot}'s. */
    private static Rotation rotationBetween(DungeonRoom pick, DungeonRoom slot) {
        String slotShape = shapeKey(slot, Rotation.NONE);
        for (Rotation rotation : Rotation.values()) {
            if (shapeKey(pick, rotation).equals(slotShape)) {
                return rotation;
            }
        }
        return null;
    }

    public static RavengardDungeon generatePacked(RavengardRoomCatalog catalog, long seed, int targetRoomCount) {
        targetRoomCount = Math.min(targetRoomCount, catalog.getRooms().size());
        RavengardDungeon best = null;
        for (int attempt = 0; attempt < 6; attempt++) {
            RavengardDungeon dungeon = generateAttempt(catalog, seed + attempt * 1000003L, targetRoomCount);
            if (best == null || dungeon.getRoomCount() > best.getRoomCount()) {
                best = dungeon;
            }
            if (best.getRoomCount() >= targetRoomCount) {
                break;
            }
        }
        return best;
    }

    private static RavengardDungeon generateAttempt(RavengardRoomCatalog catalog, long seed, int targetRoomCount) {
        RavengardDungeon dungeon = new RavengardDungeon(seed);
        dungeon.layoutRadius = layoutRadiusFor(catalog, targetRoomCount);
        Random random = new Random(seed);
        List<DungeonRoom> roomPool = catalog.getRooms();

        Set<Long> occupiedCells = new HashSet<>();
        List<PlacedSocket> openSockets = new ArrayList<>();
        Set<String> usedRoomIds = new HashSet<>();

        DungeonRoom citadel = catalog.getCitadel();
        if (citadel != null) {
            // the blueprint keeps one towering structure at the heart of every
            // map; it anchors the layout and its doors seed the room growth
            dungeon.place(new RoomPlacement(citadel, Rotation.NONE,
                            -citadel.getWidth() / 2, -citadel.getDepth() / 2),
                    occupiedCells, openSockets, usedRoomIds);
        } else {
            List<DungeonRoom> hubRooms = roomPool.stream()
                    .filter(room -> room.getSockets().size() >= 3)
                    .toList();
            DungeonRoom startRoom = hubRooms.get(random.nextInt(hubRooms.size()));
            dungeon.place(new RoomPlacement(startRoom, Rotation.NONE,
                            -startRoom.getWidth() / 2, -startRoom.getDepth() / 2),
                    occupiedCells, openSockets, usedRoomIds);
        }

        growByDoors(dungeon, roomPool, occupiedCells, openSockets, usedRoomIds, random, targetRoomCount);
        // door growth alone leaves voids between arms; the source map is a solid
        // fabric, so pack the remainder flush against existing walls, then let
        // door growth resume whenever new sockets appear
        int stall = 0;
        while (dungeon.placements.size() < targetRoomCount && stall < 400) {
            boolean placedFlush = wallFill(dungeon, roomPool, occupiedCells, openSockets, usedRoomIds, random);
            if (!placedFlush) {
                stall++;
                continue;
            }
            stall = 0;
            growByDoors(dungeon, roomPool, occupiedCells, openSockets, usedRoomIds, random, targetRoomCount);
        }
        dungeon.sealedSockets.addAll(openSockets);
        return dungeon;
    }

    private static void growByDoors(RavengardDungeon dungeon, List<DungeonRoom> roomPool,
                                    Set<Long> occupiedCells, List<PlacedSocket> openSockets,
                                    Set<String> usedRoomIds, Random random, int targetRoomCount) {
        while (dungeon.placements.size() < targetRoomCount && !openSockets.isEmpty()) {
            PlacedSocket sourceSocket = openSockets.remove(random.nextInt(openSockets.size()));
            Direction requiredSide = sourceSocket.getWorldSide().getOpposite();
            List<RoomPlacement> candidates = new ArrayList<>();
            List<DoorSocket> entrances = new ArrayList<>();
            for (DungeonRoom room : roomPool) {
                for (DoorSocket entrance : room.getSockets()) {
                    if (Math.abs(entrance.getY() - sourceSocket.getY()) > 1.5) continue;
                    for (Rotation rotation : Rotation.values()) {
                        if (entrance.getSide().rotated(rotation) != requiredSide) continue;
                        RoomPlacement candidate = alignToSocket(sourceSocket, room, entrance, rotation);
                        if (fits(candidate, occupiedCells, dungeon.layoutRadius)) {
                            candidates.add(candidate);
                            entrances.add(entrance);
                        }
                    }
                }
            }
            if (candidates.isEmpty()) {
                dungeon.sealedSockets.add(sourceSocket);
                continue;
            }
            int pickedIndex = pickSnug(candidates, occupiedCells, random);
            RoomPlacement picked = candidates.get(pickedIndex);
            DoorSocket pickedEntrance = entrances.get(pickedIndex);
            dungeon.place(picked, occupiedCells, openSockets, usedRoomIds);
            openSockets.removeIf(open -> open.placement() == picked && open.socket() == pickedEntrance);
        }
    }

    /** One attempt at packing a room flush against an existing wall (two block seam). */
    private static boolean wallFill(RavengardDungeon dungeon, List<DungeonRoom> roomPool,
                                    Set<Long> occupiedCells, List<PlacedSocket> openSockets,
                                    Set<String> usedRoomIds, Random random) {
        RoomPlacement host = dungeon.placements.get(random.nextInt(dungeon.placements.size()));
        DungeonRoom room = roomPool.get(random.nextInt(roomPool.size()));
        Rotation rotation = Rotation.values()[random.nextInt(4)];
        RoomPlacement probe = new RoomPlacement(room, rotation, 0, 0);
        int side = random.nextInt(4);
        int lateral = random.nextInt(Math.max(1, side < 2
                ? host.getFootprintWidth() + probe.getFootprintWidth()
                : host.getFootprintDepth() + probe.getFootprintDepth()));
        RoomPlacement candidate = switch (side) {
            case 0 -> new RoomPlacement(room, rotation,
                    host.originX() - probe.getFootprintWidth() + lateral,
                    host.originZ() - DOOR_SEAM_WIDTH - probe.getFootprintDepth() + 1);
            case 1 -> new RoomPlacement(room, rotation,
                    host.originX() - probe.getFootprintWidth() + lateral,
                    host.originZ() + host.getFootprintDepth() - 1 + DOOR_SEAM_WIDTH);
            case 2 -> new RoomPlacement(room, rotation,
                    host.originX() - DOOR_SEAM_WIDTH - probe.getFootprintWidth() + 1,
                    host.originZ() - probe.getFootprintDepth() + lateral);
            default -> new RoomPlacement(room, rotation,
                    host.originX() + host.getFootprintWidth() - 1 + DOOR_SEAM_WIDTH,
                    host.originZ() - probe.getFootprintDepth() + lateral);
        };
        if (!fits(candidate, occupiedCells, dungeon.layoutRadius)) {
            return false;
        }
        dungeon.place(candidate, occupiedCells, openSockets, usedRoomIds);
        return true;
    }

    private void place(RoomPlacement placement, Set<Long> occupiedCells,
                       List<PlacedSocket> openSockets, Set<String> usedRoomIds) {
        placements.add(placement);
        usedRoomIds.add(placement.room().getId());
        placement.forEachMaskCell((localX, localZ) -> {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    occupiedCells.add(cellKey(placement.originX() + localX + dx,
                            placement.originZ() + localZ + dz));
                }
            }
        });
        for (DoorSocket socket : placement.room().getSockets()) {
            openSockets.add(new PlacedSocket(placement, socket));
        }
    }

    private static RoomPlacement alignToSocket(PlacedSocket sourceSocket, DungeonRoom room,
                                               DoorSocket entrance, Rotation rotation) {
        RoomPlacement probe = new RoomPlacement(room, rotation, 0, 0);
        double[] entranceLocal = probe.toPlacementFrame(entrance.getX(), entrance.getZ());
        int entranceX = (int) Math.round(entranceLocal[0]);
        int entranceZ = (int) Math.round(entranceLocal[1]);
        RoomPlacement source = sourceSocket.placement();
        return switch (sourceSocket.getWorldSide()) {
            case EAST -> new RoomPlacement(room, rotation,
                    source.originX() + source.getFootprintWidth() - 1 + DOOR_SEAM_WIDTH,
                    sourceSocket.getWorldZ() - entranceZ);
            case WEST -> new RoomPlacement(room, rotation,
                    source.originX() - DOOR_SEAM_WIDTH - probe.getFootprintWidth() + 1,
                    sourceSocket.getWorldZ() - entranceZ);
            case SOUTH -> new RoomPlacement(room, rotation,
                    sourceSocket.getWorldX() - entranceX,
                    source.originZ() + source.getFootprintDepth() - 1 + DOOR_SEAM_WIDTH);
            case NORTH -> new RoomPlacement(room, rotation,
                    sourceSocket.getWorldX() - entranceX,
                    source.originZ() - DOOR_SEAM_WIDTH - probe.getFootprintDepth() + 1);
        };
    }

    /**
     * Chooses among fitting candidates by how tightly they hug what is already
     * built: candidates whose border touches the most occupied cells score
     * highest, which packs the fabric like the source map instead of snaking.
     */
    private static int pickSnug(List<RoomPlacement> candidates, Set<Long> occupiedCells, Random random) {
        int bestScore = -1;
        List<Integer> best = new ArrayList<>();
        for (int index = 0; index < candidates.size(); index++) {
            RoomPlacement candidate = candidates.get(index);
            int[] contact = {0};
            candidate.forEachMaskCell((localX, localZ) -> {
                int worldX = candidate.originX() + localX;
                int worldZ = candidate.originZ() + localZ;
                for (int dx = -2; dx <= 2; dx += 4) {
                    if (occupiedCells.contains(cellKey(worldX + dx, worldZ))) contact[0]++;
                }
                for (int dz = -2; dz <= 2; dz += 4) {
                    if (occupiedCells.contains(cellKey(worldX, worldZ + dz))) contact[0]++;
                }
            });
            if (contact[0] > bestScore) {
                bestScore = contact[0];
                best.clear();
                best.add(index);
            } else if (contact[0] == bestScore) {
                best.add(index);
            }
        }
        return best.get(random.nextInt(best.size()));
    }

    private static boolean fits(RoomPlacement placement, Set<Long> occupiedCells, int layoutRadius) {
        boolean[] blocked = {false};
        placement.forEachMaskCell((localX, localZ) -> {
            if (blocked[0]) return;
            int worldX = placement.originX() + localX;
            int worldZ = placement.originZ() + localZ;
            if (Math.abs(worldX) > layoutRadius || Math.abs(worldZ) > layoutRadius
                    || occupiedCells.contains(cellKey(worldX, worldZ))) {
                blocked[0] = true;
            }
        });
        return !blocked[0];
    }

    private static long cellKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    /** Plan view, one character per four blocks, in the style of the catacombs printout. */
    @Override
    public String toString() {
        final int blocksPerCharacter = 4;
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
        for (RoomPlacement placement : placements) {
            minX = Math.min(minX, placement.originX());
            maxX = Math.max(maxX, placement.originX() + placement.getFootprintWidth());
            minZ = Math.min(minZ, placement.originZ());
            maxZ = Math.max(maxZ, placement.originZ() + placement.getFootprintDepth());
        }
        int columns = (maxX - minX) / blocksPerCharacter + 2;
        int rows = (maxZ - minZ) / blocksPerCharacter + 2;
        char[][] canvas = new char[rows][columns];
        for (char[] row : canvas) Arrays.fill(row, ' ');

        for (int index = 0; index < placements.size(); index++) {
            RoomPlacement placement = placements.get(index);
            char label = index == 0 ? 'S' : Character.toUpperCase(placement.room().getColor().charAt(0));
            int left = (placement.originX() - minX) / blocksPerCharacter;
            int right = (placement.originX() + placement.getFootprintWidth() - 1 - minX) / blocksPerCharacter;
            int top = (placement.originZ() - minZ) / blocksPerCharacter;
            int bottom = (placement.originZ() + placement.getFootprintDepth() - 1 - minZ) / blocksPerCharacter;
            for (int column = left; column <= right; column++) {
                canvas[top][column] = '-';
                canvas[bottom][column] = '-';
            }
            for (int row = top; row <= bottom; row++) {
                canvas[row][left] = '|';
                canvas[row][right] = '|';
            }
            canvas[(top + bottom) / 2][(left + right) / 2] = label;
        }
        for (RoomPlacement placement : placements) {
            for (DoorSocket socket : placement.room().getSockets()) {
                PlacedSocket placed = new PlacedSocket(placement, socket);
                int column = (placed.getWorldX() - minX) / blocksPerCharacter;
                int row = (placed.getWorldZ() - minZ) / blocksPerCharacter;
                if (row >= 0 && row < rows && column >= 0 && column < columns) {
                    canvas[row][column] = '+';
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append("seed=").append(seed).append(" rooms=").append(placements.size())
                .append(" sealed=").append(sealedSockets.size())
                .append(" bounds=").append(maxX - minX).append('x').append(maxZ - minZ).append('\n');
        for (char[] row : canvas) {
            builder.append(new String(row)).append('\n');
        }
        return builder.toString();
    }
}
