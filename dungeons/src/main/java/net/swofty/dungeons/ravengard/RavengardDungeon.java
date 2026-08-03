package net.swofty.dungeons.ravengard;

import lombok.Getter;
import net.swofty.dungeons.GameDungeon;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog.DoorSocket;
import net.swofty.dungeons.ravengard.RavengardRoomCatalog.DungeonRoom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Getter
public final class RavengardDungeon extends GameDungeon {
    public static final int DOOR_SEAM_WIDTH = 2;

    private final List<RoomPlacement> placements = new ArrayList<>();
    private final List<PlacedSocket> sealedSockets = new ArrayList<>();
    private final long seed;

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

    public static RavengardDungeon generate(RavengardRoomCatalog catalog, long seed, int targetRoomCount) {
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
        Random random = new Random(seed);
        List<DungeonRoom> roomPool = catalog.getRooms();

        List<DungeonRoom> hubRooms = roomPool.stream()
                .filter(room -> room.getSockets().size() >= 3)
                .toList();
        DungeonRoom startRoom = hubRooms.get(random.nextInt(hubRooms.size()));

        Set<Long> occupiedCells = new HashSet<>();
        List<PlacedSocket> openSockets = new ArrayList<>();
        Set<String> usedRoomIds = new HashSet<>();

        dungeon.place(new RoomPlacement(startRoom, Rotation.NONE,
                        -startRoom.getWidth() / 2, -startRoom.getDepth() / 2),
                occupiedCells, openSockets, usedRoomIds);

        while (dungeon.placements.size() < targetRoomCount && !openSockets.isEmpty()) {
            PlacedSocket sourceSocket = openSockets.remove(random.nextInt(openSockets.size()));
            Direction requiredSide = sourceSocket.getWorldSide().getOpposite();

            List<RoomPlacement> candidatePlacements = new ArrayList<>();
            List<DoorSocket> candidateEntrances = new ArrayList<>();
            boolean allowReuse = usedRoomIds.size() >= roomPool.size() / 2;
            for (DungeonRoom room : roomPool) {
                if (!allowReuse && usedRoomIds.contains(room.getId())) continue;
                for (DoorSocket entrance : room.getSockets()) {
                    if (Math.abs(entrance.getY() - sourceSocket.getY()) > 1.5) continue;
                    for (Rotation rotation : Rotation.values()) {
                        if (entrance.getSide().rotated(rotation) != requiredSide) continue;
                        RoomPlacement candidate = alignToSocket(sourceSocket, room, entrance, rotation);
                        if (fits(candidate, occupiedCells)) {
                            candidatePlacements.add(candidate);
                            candidateEntrances.add(entrance);
                        }
                    }
                }
            }
            if (candidatePlacements.isEmpty()) {
                dungeon.sealedSockets.add(sourceSocket);
                continue;
            }
            // dead end rooms starve the layout when few sockets stay open, so while
            // the dungeon still needs to grow, prefer rooms that add new doorways
            boolean needsGrowth = openSockets.size() < 4
                    && dungeon.placements.size() + openSockets.size() < targetRoomCount;
            if (needsGrowth) {
                List<Integer> growing = new ArrayList<>();
                for (int index = 0; index < candidatePlacements.size(); index++) {
                    if (candidatePlacements.get(index).room().getSockets().size() >= 2) {
                        growing.add(index);
                    }
                }
                if (!growing.isEmpty()) {
                    int growthPick = growing.get(random.nextInt(growing.size()));
                    RoomPlacement picked = candidatePlacements.get(growthPick);
                    DoorSocket pickedEntrance = candidateEntrances.get(growthPick);
                    dungeon.place(picked, occupiedCells, openSockets, usedRoomIds);
                    openSockets.removeIf(open -> open.placement() == picked && open.socket() == pickedEntrance);
                    continue;
                }
            }
            int pickedIndex = random.nextInt(candidatePlacements.size());
            RoomPlacement picked = candidatePlacements.get(pickedIndex);
            DoorSocket pickedEntrance = candidateEntrances.get(pickedIndex);
            dungeon.place(picked, occupiedCells, openSockets, usedRoomIds);
            openSockets.removeIf(open -> open.placement() == picked && open.socket() == pickedEntrance);
        }
        // second chance: fizzled layouts revisit their sealed doorways with the
        // whole pool available before giving up
        if (dungeon.placements.size() < targetRoomCount && !dungeon.sealedSockets.isEmpty()) {
            openSockets.addAll(dungeon.sealedSockets);
            dungeon.sealedSockets.clear();
            usedRoomIds.clear();
            while (dungeon.placements.size() < targetRoomCount && !openSockets.isEmpty()) {
                PlacedSocket sourceSocket = openSockets.remove(random.nextInt(openSockets.size()));
                Direction requiredSide = sourceSocket.getWorldSide().getOpposite();
                List<RoomPlacement> retryPlacements = new ArrayList<>();
                List<DoorSocket> retryEntrances = new ArrayList<>();
                for (DungeonRoom room : roomPool) {
                    for (DoorSocket entrance : room.getSockets()) {
                        if (Math.abs(entrance.getY() - sourceSocket.getY()) > 1.5) continue;
                        for (Rotation rotation : Rotation.values()) {
                            if (entrance.getSide().rotated(rotation) != requiredSide) continue;
                            RoomPlacement candidate = alignToSocket(sourceSocket, room, entrance, rotation);
                            if (fits(candidate, occupiedCells)) {
                                retryPlacements.add(candidate);
                                retryEntrances.add(entrance);
                            }
                        }
                    }
                }
                if (retryPlacements.isEmpty()) {
                    dungeon.sealedSockets.add(sourceSocket);
                    continue;
                }
                int retryPick = random.nextInt(retryPlacements.size());
                RoomPlacement picked = retryPlacements.get(retryPick);
                DoorSocket pickedEntrance = retryEntrances.get(retryPick);
                dungeon.place(picked, occupiedCells, openSockets, usedRoomIds);
                openSockets.removeIf(open -> open.placement() == picked && open.socket() == pickedEntrance);
            }
        }
        dungeon.sealedSockets.addAll(openSockets);
        return dungeon;
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

    private static boolean fits(RoomPlacement placement, Set<Long> occupiedCells) {
        boolean[] blocked = {false};
        placement.forEachMaskCell((localX, localZ) -> {
            if (!blocked[0] && occupiedCells.contains(
                    cellKey(placement.originX() + localX, placement.originZ() + localZ))) {
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
