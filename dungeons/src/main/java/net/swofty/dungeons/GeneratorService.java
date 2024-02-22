package net.swofty.dungeons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class GeneratorService {
    private final DungeonsData data;
    @Getter
    private GenerationStage currentStage = GenerationStage.INITIALIZATION;
    private CompletableFuture<SkyBlockDungeon> generationFuture;
    @Getter
    private long generationStartTime;

    public CompletableFuture<SkyBlockDungeon> generate() {
        CompletableFuture<SkyBlockDungeon> future = new CompletableFuture<>();
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
                    if (!room.isAutomaticallyPlace()) continue;
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

            SkyBlockDungeon dungeon = new SkyBlockDungeon();
            DungeonUtilities.loopOverDungeonRooms(data).forEach(values -> {
                int width = values.getKey();
                int height = values.getValue();

                dungeon.setRoom(width, height, SkyBlockDungeon.DungeonRoom.ofBase());
            });

            final int[] fairyPositions = {0, 0};
            // Not async, we need this done before generating the critical path
            fairyPosition.thenAccept(fairyPos -> {
                fairyPositions[0] = fairyPos[0];
                fairyPositions[1] = fairyPos[1];
                dungeon.setRoom(fairyPositions[0], fairyPositions[1], new SkyBlockDungeon.DungeonRoom(DungeonRoomType.FAIRY));
            });

            // We can use the entranceAndExits future to get the entrance and exit points
            entranceAndExits.thenAcceptAsync(entranceAndExit -> {
                int entranceX = entranceAndExit[0];
                int exitX = entranceAndExit[1];

                dungeon.setRoom(entranceX, 0, new SkyBlockDungeon.DungeonRoom(DungeonRoomType.ENTRANCE));
                dungeon.setRoom(exitX, data.getHeight() - 1, new SkyBlockDungeon.DungeonRoom(DungeonRoomType.EXIT));

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

                // Move to side path generation
                currentStage = GenerationStage.SIDE_PATHS;

                // Thought is that we run sidePath cycles until the dungeon is filled
                // However, only rooms with a stage less or same as the sidePath cycle index
                // are considered for expansion
                // So stage 1 rooms are expanded in the first cycle, stage 1 & 2 rooms in the second, etc
                int amountOfRooms = data.getWidth() * data.getHeight();
                int filledRooms = path.size();

                Map<Integer, List<int[]>> stageToRooms = new HashMap<>();
                // Initialize rooms by stage, excluding entrance and exit explicitly.
                for (int[] cell : path) {
                    if ((cell[0] == entranceX && cell[1] == 0) || (cell[0] == exitX && cell[1] == data.getHeight() - 1)) {
                        continue;
                    }
                    stageToRooms.computeIfAbsent(cell[2], k -> new ArrayList<>()).add(new int[]{cell[0], cell[1], cell[2]});
                }

                for (int sidePathCycle = 1; filledRooms < amountOfRooms; sidePathCycle++) {
                    List<int[]> roomsToExpand = new ArrayList<>();
                    for (int stage = 1; stage <= sidePathCycle; stage++) {
                        List<int[]> currentStageRooms = stageToRooms.getOrDefault(stage, new ArrayList<>());
                        for (int[] cell : currentStageRooms) {
                            List<int[]> freeNeighbours = DungeonUtilities.getFreeNeighbours(cell[0], cell[1], dungeon, data);
                            for (int[] neighbour : freeNeighbours) {
                                dungeon.connectRooms(cell[0], cell[1], neighbour[0], neighbour[1]);
                                dungeon.getRoom(neighbour[0], neighbour[1]).setStage(cell[2]);
                                filledRooms++;
                                roomsToExpand.add(new int[]{neighbour[0], neighbour[1], cell[2]});
                            }
                        }
                    }
                    // Add newly filled rooms to consider for the next cycle.
                    roomsToExpand.forEach(room -> stageToRooms.computeIfAbsent(room[2], k -> new ArrayList<>()).add(room));
                }

                // Move to side room assignment stage
                currentStage = GenerationStage.ROOM_ASSIGNMENT;
                // We want to find rooms with only one door, and then assign a room to them
                // until we have the amount needed for each room type
                assignRoomsAsync(roomAmounts.join(), dungeon);

                // Move to corridor assignment stage
                currentStage = GenerationStage.CORRIDOR_ASSIGNMENT;
                // Randomly find base rooms with the same stage and connect them
                assignCorridors(dungeon);

                // Move to final touches stage
                currentStage = GenerationStage.FINAL_TOUCHES;
                // If there are two base rooms of the same type and stage right next to eachother
                // add a chance to connect them with a door
                for (int y = 0; y < data.getHeight(); y++) {
                    for (int x = 0; x < data.getWidth(); x++) {
                        SkyBlockDungeon.DungeonRoom room = dungeon.getRoom(x, y);
                        if (room.getRoomType() != DungeonRoomType.BASE) continue; // Skip if room already has a type

                        List<int[]> adjacentBases = DungeonUtilities.getAdjacentBaseRooms(x, y, dungeon, data);
                        for (int[] adjacent : adjacentBases) {
                            SkyBlockDungeon.DungeonRoom adjacentRoom = dungeon.getRoom(adjacent[0], adjacent[1]);
                            if (adjacentRoom.getRoomType() != DungeonRoomType.BASE) continue; // Skip if adjacent room already has a type
                            if (adjacentRoom.getStage() != room.getStage()) continue; // Skip if adjacent room is not the same stage

                            if (Math.random() > 0.5) {
                                dungeon.connectRooms(x, y, adjacent[0], adjacent[1]);
                            }
                        }
                    }
                }

                future.complete(dungeon);
            });
        });

        return future;
    }

    public void assignRoomsAsync(Map<DungeonRoomType, Integer> roomAmounts, SkyBlockDungeon dungeon) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        roomAmounts.forEach((type, amount) -> {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                AtomicInteger roomsAssigned = new AtomicInteger(0);
                while (roomsAssigned.get() < amount) {
                    for (int y = 0; y < data.getHeight(); y++) {
                        for (int x = 0; x < data.getWidth(); x++) {
                            SkyBlockDungeon.DungeonRoom room = dungeon.getRoom(x, y);
                            if (room.getRoomType() != DungeonRoomType.BASE) continue; // Skip if room already has a type
                            if (room.isCritical()) continue; // Skip if room is critical

                            // Random chance to skip this room, note the modification to ensure thread safety
                            if (Math.random() > 0.2) continue;

                            // Convert to async call
                            int finalX = x;
                            int finalY = y;
                            CompletableFuture<List<int[]>> doorsFuture = CompletableFuture.supplyAsync(() ->
                                    dungeon.getDoors(finalX, finalY), executor);
                            int finalX1 = x;
                            int finalY1 = y;
                            doorsFuture.thenAccept(doors -> {
                                if (doors.size() == 1) {
                                    room.setRoomType(type);

                                    dungeon.setRoom(finalX1, finalY1, room);
                                    if (roomsAssigned.incrementAndGet() >= amount) {
                                        return; // Exit if enough rooms have been assigned
                                    }
                                }
                            }).join(); // This join is to ensure that the task completes before moving on
                        }
                        if (roomsAssigned.get() >= amount) {
                            break; // Break outer loop if enough rooms have been assigned
                        }
                    }
                }
            }, executor);
            futures.add(future);
        });

        // Wait for all futures to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        executor.shutdown(); // Don't forget to shut down the executor
    }

    public void assignCorridors(SkyBlockDungeon dungeon) {
        Random random = new Random();
        int corridorID = 1;
        boolean[][] visited = new boolean[data.getWidth()][data.getHeight()]; // Track visited rooms

        for (int y = 0; y < data.getHeight(); y++) {
            for (int x = 0; x < data.getWidth(); x++) {
                SkyBlockDungeon.DungeonRoom initialRoom = dungeon.getRoom(x, y);
                if (!visited[x][y] && initialRoom.getRoomType() == DungeonRoomType.BASE) {
                    List<int[]> potentialCorridorSet = new ArrayList<>();
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{x, y});
                    int currentStage = initialRoom.getStage(); // Ensure same stage for corridor consistency

                    while (!queue.isEmpty() && potentialCorridorSet.size() < 3) {
                        int[] current = queue.poll();
                        int curX = current[0], curY = current[1];

                        if (visited[curX][curY]) continue;
                        visited[curX][curY] = true;

                        SkyBlockDungeon.DungeonRoom currentRoom = dungeon.getRoom(curX, curY);
                        if (currentRoom.getRoomType() == DungeonRoomType.BASE && currentRoom.getStage() == currentStage) {
                            potentialCorridorSet.add(current);

                            // Check adjacent base rooms of the same stage for potential inclusion
                            List<int[]> adjacents = DungeonUtilities.getAdjacentBaseRooms(curX, curY, dungeon, data);
                            for (int[] adj : adjacents) {
                                SkyBlockDungeon.DungeonRoom adjRoom = dungeon.getRoom(adj[0], adj[1]);
                                if (!visited[adj[0]][adj[1]] && adjRoom.getStage() == currentStage && random.nextBoolean()) {
                                    queue.offer(adj);
                                }
                            }
                        }
                    }

                    // Validate and assign corridor ID only if the set connects two or more rooms
                    if (potentialCorridorSet.size() > 1) {
                        for (int[] coords : potentialCorridorSet) {
                            SkyBlockDungeon.DungeonRoom room = dungeon.getRoom(coords[0], coords[1]);
                            room.setCorridorNumber(corridorID); // Assign corridor ID
                            dungeon.setRoom(coords[0], coords[1], room);
                        }
                        corridorID++; // Increment corridor ID for the next valid set
                    }
                }
            }
        }
    }

    public enum GenerationStage {
        INITIALIZATION,
        CRITICAL_PATH,
        SIDE_PATHS,
        ROOM_ASSIGNMENT,
        CORRIDOR_ASSIGNMENT,
        FINAL_TOUCHES
    }
}
