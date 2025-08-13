package net.swofty.commons;

import lombok.Getter;

@Getter
public enum ServerType {
    SKYBLOCK_ISLAND(true),
    SKYBLOCK_HUB(true),
    DUNGEON_HUB(true),
    SKYBLOCK_THE_FARMING_ISLANDS(true),
    PROTOTYPE_LOBBY(false),
    BEDWARS_LOBBY(false),
    BEDWARS_GAME(false),
    ;

    private final boolean isSkyBlock;

    ServerType(boolean isSkyBlock) {
        this.isSkyBlock = isSkyBlock;
    }

    public static boolean isServerType(String type) {
        for (ServerType a : values())
            if (type.equalsIgnoreCase(a.name())) return true;

        return false;
    }

    public String formatName() {
        return StringUtility.toNormalCase(name());
    }
}
