package net.swofty.dungeons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HypixelDungeon {
    private Map<Map.Entry<Integer, Integer>, DungeonRoom> rooms = new HashMap<>();
    private List<DungeonDoor> doors = new ArrayList<>();

    public HypixelDungeon setRoom(int x, int y, DungeonRoom room) {
        rooms.put(Map.entry(x, y), room);
        return this;
    }

    public DungeonRoom getRoom(int x, int y) {
        return rooms.get(Map.entry(x, y));
    }

    public boolean isConnected(int x1, int y1, int x2, int y2) {
        return doors.stream().anyMatch(door -> (door.x1() == x1 && door.y1() == y1 && door.x2() == x2 && door.y2() == y2) || (door.x1() == x2 && door.y1() == y2 && door.x2() == x1 && door.y2() == y1));
    }

    public void connectRooms(int x1, int y1, int x2, int y2) {
        DungeonRoom room1 = getRoom(x1, y1);
        DungeonRoom room2 = getRoom(x2, y2);
        if (room1 == null || room2 == null) {
            throw new IllegalArgumentException("Room at " + x1 + ", " + y1 + " or " + x2 + ", " + y2 + " does not exist");
        }

        doors.add(new DungeonDoor(x1, y1, x2, y2));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // Loop over the dungeon rooms, center them and print them

        int width = rooms.keySet().stream().mapToInt(Map.Entry::getKey).max().orElse(0);
        int height = rooms.keySet().stream().mapToInt(Map.Entry::getValue).max().orElse(0);

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                DungeonRoom room = rooms.get(Map.entry(x, y));
                if (room == null) {
                    builder.append(DungeonUtilities.center(" ", 20, ' '));
                } else {
                    builder.append(DungeonUtilities.center(
                            (isConnected(x, y, x - 1, y) ? "║" : " ") +
                            room.toString() + (isConnected(x, y, x + 1, y) ? "║" : " "),
                            20, ' '));
                }
            }
            builder.append("\n");

            if (y < height) {
                for (int x = 0; x <= width; x++) {
                    DungeonRoom room = rooms.get(Map.entry(x, y));
                    if (room == null) {
                        builder.append(DungeonUtilities.center(" ", 20, ' '));
                    } else {
                        builder.append(DungeonUtilities.center(
                                (isConnected(x, y, x, y + 1) ? "══" : "   "),
                                20, ' '));
                    }
                }
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    @RequiredArgsConstructor
    @Getter
    public static class DungeonRoom {
        private final DungeonRoomType roomType;
        @Setter
        private boolean isCritical;
        @Setter
        private int stage = 0;

        @Override
        public String toString() {
            return roomType.name() + (isCritical ? " (C)" : "") + (stage > 0 ? " (S-" + stage + ")" : "");
        }

        public static DungeonRoom ofBase() {
            return new DungeonRoom(DungeonRoomType.BASE);
        }
    }

    public record DungeonDoor(int x1, int y1, int x2, int y2) { }
}
