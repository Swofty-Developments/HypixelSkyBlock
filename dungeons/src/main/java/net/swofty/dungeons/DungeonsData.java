package net.swofty.dungeons;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class DungeonsData {
    private final Map<DungeonRoomType, RoomData> roomData;
    @Getter
    private final Integer width;
    @Getter
    private final Integer height;

    public DungeonsData(Integer width, Integer height) {
        this.width = width;
        this.height = height;
        this.roomData = new HashMap<>();
    }

    public DungeonsData with(DungeonRoomType room, RoomData data) {
        roomData.put(room, data);
        return this;
    }

    public RoomData getDataForOrNull(DungeonRoomType room) {
        return roomData.get(room);
    }

    public record RoomData(int minimumAmount, int maximumAmount) {}
}
