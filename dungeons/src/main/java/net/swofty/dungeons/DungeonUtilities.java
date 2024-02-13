package net.swofty.dungeons;

import java.util.*;
import java.util.stream.Stream;

public class DungeonUtilities {
    public static Stream<Map.Entry<Integer, Integer>> loopOverDungeonRooms(DungeonsData data) {
        return Stream.iterate(0, i -> i < data.getWidth() * data.getHeight(), i -> i + 1)
                .map(i -> Map.entry(i % data.getWidth(), i / data.getHeight()));
    }

    public static void asyncPrintPerformance(long startTime, String identifier) {
        Thread.startVirtualThread(() -> {
            long time = System.currentTimeMillis() - startTime;
            System.out.println("Dungeon Performance: " + identifier + " took " + time + "ms");
        });
    }

    public static List<int[]> aStar(int startX, int startY, int endX, int endY, int width, int height) {
        PriorityQueue<int[]> openSet = new PriorityQueue<>(Comparator.comparingInt(cell -> cell[2]));
        openSet.add(new int[]{startX, startY, 0});
        Map<String, int[]> cameFrom = new HashMap<>();
        Map<String, Integer> costSoFar = new HashMap<>();
        costSoFar.put(startX + "," + startY, 0);

        while (!openSet.isEmpty()) {
            int[] current = openSet.poll();

            if (current[0] == endX && current[1] == endY) {
                List<int[]> path = new ArrayList<>();
                while (current != null) {
                    path.addFirst(new int[]{current[0], current[1]});
                    current = cameFrom.get(current[0] + "," + current[1]);
                }
                return path;
            }

            for (int[] next : getNeighbors(current[0], current[1], width, height)) {
                int newCost = costSoFar.get(current[0] + "," + current[1]) + 1;
                if (!costSoFar.containsKey(next[0] + "," + next[1]) || newCost < costSoFar.get(next[0] + "," + next[1])) {
                    costSoFar.put(next[0] + "," + next[1], newCost);
                    int priority = newCost + Math.abs(endX - next[0]) + Math.abs(endY - next[1]);
                    openSet.add(new int[]{next[0], next[1], priority});
                    cameFrom.put(next[0] + "," + next[1], current);
                }
            }
        }

        return new ArrayList<>();
    }

    private static List<int[]> getNeighbors(int x, int y, int width, int height) {
        List<int[]> neighbors = new ArrayList<>();
        if (x > 0) neighbors.add(new int[]{x - 1, y});
        if (y > 0) neighbors.add(new int[]{x, y - 1});
        if (x < width - 1) neighbors.add(new int[]{x + 1, y});
        if (y < height - 1) neighbors.add(new int[]{x, y + 1});
        return neighbors;
    }

    public static void asyncPrintDungeon(HypixelDungeon dungeon) {
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
