package net.swofty.dungeons;

import java.util.*;
import java.util.stream.Stream;

public class DungeonUtilities {
    public static Stream<Map.Entry<Integer, Integer>> loopOverDungeonRooms(DungeonsData data) {
        return Stream.iterate(0, i -> i < data.getWidth() * data.getHeight(), i -> i + 1)
                .map(i -> Map.entry(i % data.getWidth(), i / data.getHeight()));
    }

    public static List<int[]> getFreeNeighbours(int x, int y, SkyBlockDungeon dungeon, DungeonsData data) {
        List<int[]> freeNeighbours = new ArrayList<>();
        if (x > 0 && dungeon.getRoom(x - 1, y).getStage() == 0) freeNeighbours.add(new int[]{x - 1, y});
        if (y > 0 && dungeon.getRoom(x, y - 1).getStage() == 0) freeNeighbours.add(new int[]{x, y - 1});
        if (x < data.getWidth() - 1 && dungeon.getRoom(x + 1, y).getStage() == 0)
            freeNeighbours.add(new int[]{x + 1, y});
        if (y < data.getHeight() - 1 && dungeon.getRoom(x, y + 1).getStage() == 0)
            freeNeighbours.add(new int[]{x, y + 1});
        return freeNeighbours;
    }

    public static List<int[]> getAdjacentBaseRooms(int x, int y, SkyBlockDungeon dungeon, DungeonsData data) {
        List<int[]> neighbours = new ArrayList<>();
        if (x > 0 && dungeon.getRoom(x - 1, y).getRoomType() == DungeonRoomType.BASE)
            neighbours.add(new int[]{x - 1, y});
        if (y > 0 && dungeon.getRoom(x, y - 1).getRoomType() == DungeonRoomType.BASE)
            neighbours.add(new int[]{x, y - 1});
        if (x < data.getWidth() - 1 && dungeon.getRoom(x + 1, y).getRoomType() == DungeonRoomType.BASE)
            neighbours.add(new int[]{x + 1, y});
        if (y < data.getHeight() - 1 && dungeon.getRoom(x, y + 1).getRoomType() == DungeonRoomType.BASE)
            neighbours.add(new int[]{x, y + 1});
        return neighbours;
    }

    public static void asyncPrintPerformance(long startTime, String identifier) {
        Thread.startVirtualThread(() -> {
            long time = System.currentTimeMillis() - startTime;
            System.out.println("Dungeon Performance: " + identifier + " took " + time + "ms");
        });
    }

    public static List<int[]> aStar(List<int[]> alreadyVisited,
                                    int startX, int startY, int endX, int endY, int width, int height) {
        PriorityQueue<int[]> openSet = new PriorityQueue<>(Comparator.comparingInt(cell -> cell[2]));
        openSet.add(new int[]{startX, startY, 0}); // Add start position to open set
        Map<String, int[]> cameFrom = new HashMap<>();
        Map<String, Integer> costSoFar = new HashMap<>();
        costSoFar.put(startX + "," + startY, 0);

        // Ensure start position is not considered already visited
        alreadyVisited.removeIf(cell -> cell[0] == startX && cell[1] == startY);

        while (!openSet.isEmpty()) {
            int[] current = openSet.poll();

            if (current[0] == endX && current[1] == endY) {
                return reconstructPath(cameFrom, startX, startY, current); // Ensure start is included in path
            }

            for (int[] neighbor : getNeighbors(current[0], current[1], width, height)) {
                // Convert neighbor to a String key to check if visited
                String neighborKey = neighbor[0] + "," + neighbor[1];
                if (alreadyVisited.stream().anyMatch(cell -> cell[0] == neighbor[0] && cell[1] == neighbor[1])) continue; // Skip already visited

                int newCost = costSoFar.getOrDefault(current[0] + "," + current[1], Integer.MAX_VALUE) + 1;
                if (!costSoFar.containsKey(neighborKey) || newCost < costSoFar.get(neighborKey)) {
                    costSoFar.put(neighborKey, newCost);
                    int priority = newCost + heuristic(neighbor[0], neighbor[1], endX, endY);
                    openSet.add(new int[]{neighbor[0], neighbor[1], priority});
                    cameFrom.put(neighborKey, current);
                }
            }
        }

        return new ArrayList<>(); // Return empty path if goal is not reachable
    }

    private static int heuristic(int x, int y, int endX, int endY) {
        // Using Manhattan distance as the heuristic
        return Math.abs(x - endX) + Math.abs(y - endY);
    }

    private static List<int[]> getNeighbors(int x, int y, int width, int height) {
        List<int[]> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(new int[]{x - 1, y});
        if (y > 0) neighbors.add(new int[]{x, y - 1});
        if (x < width - 1) neighbors.add(new int[]{x + 1, y});
        if (y < height - 1) neighbors.add(new int[]{x, y + 1});
        return neighbors;
    }

    private static List<int[]> reconstructPath(Map<String, int[]> cameFrom, int startX, int startY, int[] current) {
        List<int[]> path = new ArrayList<>();
        while (cameFrom.containsKey(current[0] + "," + current[1])) {
            path.addFirst(current); // Add to the beginning of the list
            current = cameFrom.get(current[0] + "," + current[1]);
        }
        path.addFirst(new int[]{startX, startY}); // Ensure start position is included
        return path;
    }

    public static void asyncPrintDungeon(SkyBlockDungeon dungeon) {
        Thread.startVirtualThread(() -> {
            System.out.println(dungeon);
        });
    }

    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length())
            return s;

        StringBuilder sb = new StringBuilder(size);
        sb.append(String.valueOf(pad).repeat((size - s.length()) / 2));
        sb.append(s);
        while (sb.length() < size) {
            sb.append(pad);
        }
        return sb.toString();
    }
}
