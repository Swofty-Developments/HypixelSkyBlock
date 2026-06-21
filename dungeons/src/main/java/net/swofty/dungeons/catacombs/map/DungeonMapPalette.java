package net.swofty.dungeons.catacombs.map;

import net.swofty.dungeons.DungeonRoomType;

import java.awt.Color;

public enum DungeonMapPalette {
    ENTRANCE(DungeonRoomType.ENTRANCE, new Color(0x55CC55)),
    BASE(DungeonRoomType.BASE, new Color(0x8A6D54)),
    FAIRY(DungeonRoomType.FAIRY, new Color(0xFF77DD)),
    PUZZLE(DungeonRoomType.PUZZLE, new Color(0xAA66CC)),
    MINI_BOSS(DungeonRoomType.MINI_BOSS, new Color(0xFFAA00)),
    TRAP(DungeonRoomType.TRAP, new Color(0xCC3333)),
    BLOOD(DungeonRoomType.BLOOD, new Color(0xAA0000)),
    EXIT(DungeonRoomType.EXIT, new Color(0x333333));

    private final DungeonRoomType roomType;
    private final Color color;

    DungeonMapPalette(DungeonRoomType roomType, Color color) {
        this.roomType = roomType;
        this.color = color;
    }

    public Color color() {
        return color;
    }

    public static Color forRoom(DungeonRoomType type) {
        for (DungeonMapPalette value : values()) {
            if (value.roomType == type) {
                return value.color;
            }
        }
        return BASE.color;
    }
}
