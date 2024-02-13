package net.swofty.dungeons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class HypixelDungeon {
    private Map<Map.Entry<Integer, Integer>, DungeonRoom> rooms = new HashMap<>();

    public HypixelDungeon setRoom(int x, int y, DungeonRoom room) {
        rooms.put(Map.entry(x, y), room);
        return this;
    }

    public DungeonRoom getRoom(int x, int y) {
        return rooms.get(Map.entry(x, y));
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
                    builder.append(DungeonUtilities.center(room.toString(), 20, ' '));
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    @RequiredArgsConstructor
    @Getter
    public static class DungeonRoom {
        private final DungeonRooms roomType;
        @Setter
        private boolean isCritical;
        @Setter
        private boolean postFairy;

        @Override
        public String toString() {
            return roomType.name() + (isCritical ? " (C)" : "") + (postFairy ? " (PF)" : "");
        }

        public static DungeonRoom ofBase() {
            return new DungeonRoom(DungeonRooms.BASE);
        }
    }
}
