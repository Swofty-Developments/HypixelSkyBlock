package net.swofty.dungeons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
                dungeon.setRoom(fairyPositions[0], fairyPositions[1], new HypixelDungeon.DungeonRoom(DungeonRooms.FAIRY));
            });

            // We can use the entranceAndExits future to get the entrance and exit points
            entranceAndExits.thenAcceptAsync(entranceAndExit -> {
                DungeonUtilities.asyncPrintPerformance(generationStartTime, "3");
                int entranceX = entranceAndExit[0];
                int exitX = entranceAndExit[1];

                dungeon.setRoom(entranceX, 0, new HypixelDungeon.DungeonRoom(DungeonRooms.ENTRANCE));
                dungeon.setRoom(exitX, data.getHeight() - 1, new HypixelDungeon.DungeonRoom(DungeonRooms.EXIT));

                // Move to critical path generation
                currentStage = GenerationStage.CRITICAL_PATH;
                generateCriticalPath(dungeon, entranceX, exitX, data.getHeight() - 1, fairyPositions[0], fairyPositions[1]);
            });
        });

        return future;
    }

    private void generateCriticalPath(HypixelDungeon dungeon,
                                      int entranceX, int exitX, int exitY,
                                      int fairyX, int fairyY) {
        // The thought is that here, we have our entrance and exit, and it is now time to create the link
        // between the two. This is the critical path, and it is the most important part of the dungeon route.
        // This uses the A* algorithm with randomization built in to create a path between the two points.
        // The fairyX and fairyY is the required "midpoint", and has to be part of the path.
        DungeonUtilities.asyncPrintPerformance(generationStartTime, "aStar start");
        List<int[]> pathFromEntranceToFairy = DungeonUtilities.aStar(entranceX, 0, fairyX, fairyY,
                data.getWidth(), data.getHeight());
        List<int[]> pathFromFairyToExit = DungeonUtilities.aStar(fairyX, fairyY + 1, exitX, exitY,
                data.getWidth(), data.getHeight());
        DungeonUtilities.asyncPrintPerformance(generationStartTime, "aStar end");

        // Combine the two paths
        List<int[]> path = new ArrayList<>();
        path.addAll(pathFromEntranceToFairy);
        path.addAll(pathFromFairyToExit);

        // Update the dungeon with the path
        for (int[] cell : path) {
            dungeon.getRoom(cell[0], cell[1]).setCritical(true);
            if (pathFromFairyToExit.contains(cell)) {
                dungeon.getRoom(cell[0], cell[1]).setPostFairy(true);
            }
        }

        DungeonUtilities.asyncPrintPerformance(generationStartTime, "randomization end");
        DungeonUtilities.asyncPrintDungeon(dungeon);
    }

    public enum GenerationStage {
        INITIALIZATION,
        CRITICAL_PATH,
        SIDE_PATHS,
        ROOM_ASSIGNMENT,
    }
}
