package net.swofty.dungeons.catacombs.classes;

public enum DungeonClassType {
    HEALER("Healer"),
    MAGE("Mage"),
    BERSERK("Berserk"),
    ARCHER("Archer"),
    TANK("Tank");

    private final String displayName;

    DungeonClassType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
