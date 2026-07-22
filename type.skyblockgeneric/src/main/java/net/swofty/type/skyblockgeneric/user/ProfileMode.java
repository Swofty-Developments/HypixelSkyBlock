package net.swofty.type.skyblockgeneric.user;

public enum ProfileMode {
    CLASSIC("§aClassic"),
    IRONMAN("§7♲ §7Ironman");

    private final String displayName;

    ProfileMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProfileMode fromStored(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException ignored) {
            return CLASSIC;
        }
    }
}
