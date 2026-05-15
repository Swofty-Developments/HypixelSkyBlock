package net.swofty.dungeons.catacombs.puzzle;

import net.swofty.dungeons.catacombs.CatacombsFloor;

public enum CatacombsPuzzle {
    CREEPER_BEAMS("Creeper Beams", CatacombsFloor.ENTRANCE, false),
    THREE_WEIRDOS("Three Weirdos", CatacombsFloor.ENTRANCE, true),
    TIC_TAC_TOE("Tic Tac Toe", CatacombsFloor.ENTRANCE, true),
    WATER_BOARD("Water Board", CatacombsFloor.ENTRANCE, false),
    TELEPORT_MAZE("Teleport Maze", CatacombsFloor.ENTRANCE, false),
    HIGHER_OR_LOWER("Higher or Lower", CatacombsFloor.FLOOR_THREE, true),
    BOULDER("Boulder", CatacombsFloor.FLOOR_THREE, true),
    ICE_FILL("Ice Fill", CatacombsFloor.FLOOR_SEVEN, false),
    ICE_PATH("Ice Path", CatacombsFloor.FLOOR_THREE, true),
    QUIZ("Quiz", CatacombsFloor.FLOOR_FOUR, true);

    private final String displayName;
    private final CatacombsFloor minimumFloor;
    private final boolean failable;

    CatacombsPuzzle(String displayName, CatacombsFloor minimumFloor, boolean failable) {
        this.displayName = displayName;
        this.minimumFloor = minimumFloor;
        this.failable = failable;
    }

    public String displayName() {
        return displayName;
    }

    public CatacombsFloor minimumFloor() {
        return minimumFloor;
    }

    public boolean failable() {
        return failable;
    }

    public boolean canGenerate(CatacombsFloor floor) {
        return floor.ordinal() >= minimumFloor.ordinal();
    }
}
