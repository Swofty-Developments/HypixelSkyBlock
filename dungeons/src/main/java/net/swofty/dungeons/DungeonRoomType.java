package net.swofty.dungeons;

import lombok.Getter;

@Getter
public enum DungeonRoomType {
    ENTRANCE(),
    BASE(),
    FAIRY(true),
    PUZZLE(true),
    MINI_BOSS(true),
    EXIT(),
    ;

    private boolean requiresData = false;

    DungeonRoomType() {}

    DungeonRoomType(boolean requiresData) {
        this.requiresData = requiresData;
    }
}
