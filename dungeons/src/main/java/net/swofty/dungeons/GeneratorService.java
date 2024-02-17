package net.swofty.dungeons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class GeneratorService {
    private final DungeonsData data;
    @Getter
    private GenerationStage currentStage = GenerationStage.INITIALIZATION;
    private CompletableFuture<HypixelDungeon> generationFuture;
    private long generationStartTime;

    public CompletableFuture<HypixelDungeon> generate() {
        CompletableFuture<HypixelDungeon> future = new CompletableFuture<>();
        generationFuture = future;
        generationStartTime = System.currentTimeMillis();

        // Start the generation process
        Thread.startVirtualThread(() -> {
            // Generate these in the background while initialization is being done
            CompletableFuture<int[]> entranceAndExits = CompletableFuture.supplyAsync(() -> Stream.generate(() -> {
                return (int) (Math.random() * data.getWidth());
            }).limit(2).mapToInt(i -> i).toArray());

            CompletableFuture<Map<DungeonRoomType, Integer>> roomAmounts = CompletableFuture.supplyAsync(() -> {
                Map<DungeonRoomType, Integer> amounts = new HashMap<>();
                for (DungeonRoomType room : DungeonRoomType.values()) {
                    DungeonsData.RoomData roomData = data.getDataForOrNull(room);
                    if (roomData == null) continue;
                    amounts.put(room, (int) (Math.random() * (roomData.maximumAmount() - roomData.minimumAmount())) + roomData.minimumAmount());
                }
                return amounts;
            });

            // Generate a fairy position that is not adjacent to entrance or exit
            CompletableFuture<int[]> fairyPosition = entranceAndExits.thenApplyAsync(entranceAndExit -> {
                int entranceX = entranceAndExit[0];
                int exitX = entranceAndExit[1];
                int fairyX, fairyY;
                do {
                    fairyX = (int) (Math.random() * data.getWidth());
                    fairyY = (int) (Math.random() * data.getHeight());
                    // Ensure fairy is not adjacent to entrance or exit, not on the dungeon's edges, and not directly above or below the exit
                } while ((Math.abs(fairyX - entranceX) <= 1 && Math.abs(fairyY) <= 1) || // Check adjacency to entrance, including diagonals
                        (Math.abs(fairyX - exitX) <= 1 && Math.abs(fairyY - (data.getHeight() - 1)) <= 1) || // Check adjacency to exit, including diagonals
                        fairyX == 0 || fairyX == data.getWidth() - 1 || // Check if on the left or right edge
                        fairyY == 0 || fairyY == data.getHeight() - 1); // Check if on the top or bottom edge
                return new int[]{fairyX, fairyY};
            });

            HypixelDungeon dungeon = new HypixelDungeon();
            DungeonUtilities.loopOverDungeonRooms(data).forEach(values -> {
                int width = values.getKey();
                int height = values.getValue();

                dungeon.setRoom(width, height, HypixelDungeon.DungeonRoom.ofBase());
            });

            final int[] fairyPositions = {0, 0};
            // Not async, we need this done before generating the critical path
            fairyPosition.thenAccept(fairyPos -> {
                fairyPositions[0] = fairyPos[0];
                fairyPositions[1] = fairyPos[1];
                dungeon.setRoom(fairyPositions[0], fairyPositions[1], new HypixelDungeon.DungeonRoom(DungeonRoomType.FAIRY));
            });

            // We can use the entranceAndExits future to get the entrance and exit points
            entranceAndExits.thenAcceptAsync(entranceAndExit -> {
                DungeonUtilities.asyncPrintPerformance(generationStartTime, "3");
                int entranceX = entranceAndExit[0];
                int exitX = entranceAndExit[1];

                dungeon.setRoom(entranceX, 0, new HypixelDungeon.DungeonRoom(DungeonRoomType.ENTRANCE));
                dungeon.setRoom(exitX, data.getHeight() - 1, new HypixelDungeon.DungeonRoom(DungeonRoomType.EXIT));

                // Move to critical path generation
                currentStage = GenerationStage.CRITICAL_PATH;

                // Generate a random point, one before the fairy
                // This is to ensure that the critical path is not a straight line
                // They cannot be the same as the entrance, exit or the fairy room, or eachother
                // It also can't be along the top Y or bottom Y, or the left and right X
                int[] randomPointPreFairy;
                do {
                    randomPointPreFairy = new int[]{(int) (Math.random() * data.getWidth()), (int) (Math.random() * data.getHeight())};
                } while (
                        (randomPointPreFairy[0] == entranceAndExit[0] && randomPointPreFairy[1] == 0) || // Matches entrance
                                (randomPointPreFairy[0] == entranceAndExit[1] && randomPointPreFairy[1] == data.getHeight() - 1) || // Matches exit
                                (randomPointPreFairy[0] == fairyPositions[0] && randomPointPreFairy[1] == fairyPositions[1]) || // Matches fairy position
                                randomPointPreFairy[0] == 0 || randomPointPreFairy[0] == data.getWidth() - 1 || // Left or right edge
                                randomPointPreFairy[1] == 0 || randomPointPreFairy[1] == data.getHeight() - 1 // Top or bottom edge
                );

                // Generate the critical path
                List<int[]> path = new ArrayList<>();
                path.addAll(DungeonUtilities.aStar(path,
                        entranceX, 0, randomPointPreFairy[0], randomPointPreFairy[1], data.getWidth(), data.getHeight())
                        .stream().map(cell -> new int[]{cell[0], cell[1], 1}).toList());
                path.addAll(DungeonUtilities.aStar(path,
                                randomPointPreFairy[0], randomPointPreFairy[1], fairyPositions[0], fairyPositions[1], data.getWidth(), data.getHeight())
                        .stream().map(cell -> new int[]{cell[0], cell[1], 2}).toList());
                path.addAll(DungeonUtilities.aStar(path,
                                fairyPositions[0], fairyPositions[1], exitX, data.getHeight() - 1, data.getWidth(), data.getHeight())
                        .stream().map(cell -> new int[]{cell[0], cell[1], 3}).toList());

                for (int[] cell : path) {
                    dungeon.getRoom(cell[0], cell[1]).setCritical(true);
                    dungeon.getRoom(cell[0], cell[1]).setStage(cell[2]);

                    if (path.indexOf(cell) != path.size() - 1) {
                        int[] nextCell = path.get(path.indexOf(cell) + 1);
                        if (nextCell != null) {
                            dungeon.connectRooms(cell[0], cell[1], nextCell[0], nextCell[1]);
                        }
                    }
                }

                DungeonUtilities.asyncPrintPerformance(generationStartTime, "critical path done");

                // Move to side path generation
                currentStage = GenerationStage.SIDE_PATHS;

                // Thought is that we run sidePath cycles until the dungeon is filled
                // However, only rooms with a stage less or same as the sidePath cycle index
                // are considered for expansion
                // So stage 1 rooms are expanded in the first cycle, stage 1 & 2 rooms in the second, etc
                int amountOfRooms = data.getWidth() * data.getHeight();
                int filledRooms = path.size();
                List<int[]> roomsToConsider = new ArrayList<>();
                for (int[] cell : path) {
                    if (cell[0] == entranceX && cell[1] == 0) continue;
                    if (cell[0] == exitX && cell[1] == data.getHeight() - 1) continue;
                    roomsToConsider.add(new int[]{cell[0], cell[1], cell[2]});
                }
                for (int sidePathCycle = 1; filledRooms < amountOfRooms; sidePathCycle++) {
                    List<int[]> roomsToConsiderCopy = new ArrayList<>(roomsToConsider);

                    for (int[] cell : roomsToConsider) {
                        if (cell[2] > sidePathCycle)
                            continue;

                        List<int[]> freeNeighbours = DungeonUtilities.getFreeNeighbours(cell[0], cell[1], dungeon, data);
                        for (int[] neighbour : freeNeighbours) {
                            dungeon.connectRooms(cell[0], cell[1], neighbour[0], neighbour[1]);
                            dungeon.getRoom(neighbour[0], neighbour[1]).setStage(
                                    dungeon.getRoom(cell[0], cell[1]).getStage()
                            );
                            filledRooms++;
                            roomsToConsiderCopy.add(new int[]{neighbour[0], neighbour[1], cell[2]});
                        }
                    }

                    roomsToConsider = roomsToConsiderCopy;
                }

                DungeonUtilities.asyncPrintPerformance(generationStartTime, "side path generation done");
                DungeonUtilities.asyncPrintDungeon(dungeon);

                // Move to side room assignment stage
                currentStage = GenerationStage.ROOM_ASSIGNMENT;
                Map<DungeonRoomType, Integer> roomAmountsFinal = roomAmounts.join();

                // We want to find rooms wtih only one door, and then assign a room to them
                // until we have the amount needed for each room type
            });
        });

        return future;
    }

    public enum GenerationStage {
        INITIALIZATION,
        CRITICAL_PATH,
        SIDE_PATHS,
        ROOM_ASSIGNMENT,
    }
}
