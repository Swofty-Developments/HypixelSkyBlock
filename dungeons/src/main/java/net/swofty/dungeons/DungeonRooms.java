package net.swofty.dungeons;

import lombok.Getter;

@Getter
public enum DungeonRooms {
    ENTRANCE(),
    BASE(),
    FAIRY(true),
    PUZZLE(true),
    MINI_BOSS(true),
    EXIT(),
    ;

    private boolean requiresData = false;

    DungeonRooms() {}

    DungeonRooms(boolean requiresData) {
        this.requiresData = requiresData;
    }
}
