package net.swofty.type.generic.collectibles;

public enum CollectibleGamemode {
    BEDWARS("BedWars"),
    SKYWARS("SkyWars"),
    MURDER_MYSTERY("Murder Mystery"),
    PROTOTYPE("Prototype");

    private final String displayName;

    CollectibleGamemode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
