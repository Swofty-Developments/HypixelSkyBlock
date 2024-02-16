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

                // Generate two random points, one before the fairy and one after
                // This is to ensure that the critical path is not a straight line
                // They cannot be the same as the entrance, exit or the fairy room, or eachother
                int[] randomPointPreFairy, randomPointPostFairy;
                do {
                    randomPointPreFairy = new int[]{(int) (Math.random() * data.getWidth()), (int) (Math.random() * data.getHeight())};
                } while (Arrays.equals(randomPointPreFairy, entranceAndExit) || Arrays.equals(randomPointPreFairy, fairyPositions));
                do {
                    randomPointPostFairy = new int[]{(int) (Math.random() * data.getWidth()), (int) (Math.random() * data.getHeight())};
                } while (Arrays.equals(randomPointPostFairy, entranceAndExit) || Arrays.equals(randomPointPostFairy, fairyPositions) || Arrays.equals(randomPointPostFairy, randomPointPreFairy));

                System.out.println("Random Point at " + Arrays.toString(randomPointPreFairy));
                System.out.println("Fairy at " + Arrays.toString(fairyPositions));
                System.out.println("Random Point at " + Arrays.toString(randomPointPostFairy));

                generateCriticalPath(dungeon, Arrays.asList(
                        new int[]{entranceX, 0, 1},
                        new int[]{randomPointPreFairy[0], randomPointPreFairy[1], 2},
                        new int[]{fairyPositions[0], fairyPositions[1], 3},
                        new int[]{randomPointPostFairy[0], randomPointPostFairy[1], 4},
                        new int[]{exitX, data.getHeight() - 1, 5}));
            });
        });

        return future;
    }

    private void generateCriticalPath(HypixelDungeon dungeon, List<int[]> requiredPoints) {
        // The thought is that here, we have our entrance and exit, and it is now time to create the link
        // between the two. This is the critical path, and it is the most important part of the dungeon route.
        // This uses the A* algorithm with randomization built in to create a path between the two points.
        // The fairyX and fairyY is the required "midpoint", and has to be part of the path.
        DungeonUtilities.asyncPrintPerformance(generationStartTime, "aStar start");
        List<int[]> path = new ArrayList<>();
        requiredPoints.forEach(point -> {
            // If the point is the last point, we don't need to do anything
            if (requiredPoints.indexOf(point) == requiredPoints.size() - 1) return;
            int[] nextPoints = requiredPoints.get(requiredPoints.indexOf(point) + 1);

            List<int[]> aStarPath = DungeonUtilities.aStar(path,
                    point[0], point[1], nextPoints[0], nextPoints[1], data.getWidth(), data.getHeight());
            // Add stage to the path
            for (int[] cell : aStarPath) {
                path.add(new int[]{cell[0], cell[1], point[2]});
            }
        });
        DungeonUtilities.asyncPrintPerformance(generationStartTime, "aStar end");

        // Update the dungeon with the path
        for (int[] cell : path) {
            dungeon.getRoom(cell[0], cell[1]).setCritical(true);
            dungeon.getRoom(cell[0], cell[1]).setStage(cell[2]);
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
