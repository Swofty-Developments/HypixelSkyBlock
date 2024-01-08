package net.swofty.types.generic.utility;

public enum Acronym {
    TNT,
    ;

    public static boolean isAcronym(String s) {
        for (Acronym a : values())
            if (s.equalsIgnoreCase(a.name())) return true;

        return false;
    }
}
