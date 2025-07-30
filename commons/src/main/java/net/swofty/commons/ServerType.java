package net.swofty.commons;

import lombok.Getter;
@Getter
public enum ServerType {
    ISLAND,
    HUB,
    DUNGEON_HUB,
    THE_FARMING_ISLANDS;

    public static boolean isServerType(String type) {
        for (ServerType a : values())
            if (type.equalsIgnoreCase(a.name())) return true;

        return false;
    }
}
