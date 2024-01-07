package net.swofty.commons;

public enum ServerType {
    ISLAND,
    VILLAGE,
    ;

    public static boolean isServerType(String type) {
        for (ServerType a : values())
            if (type.equalsIgnoreCase(a.name())) return true;

        return false;
    }
}
