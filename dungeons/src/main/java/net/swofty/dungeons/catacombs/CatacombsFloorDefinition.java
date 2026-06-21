package net.swofty.dungeons.catacombs;

import net.swofty.dungeons.catacombs.boss.CatacombsBossEncounter;
import net.swofty.dungeons.catacombs.puzzle.CatacombsPuzzle;
import net.swofty.dungeons.catacombs.run.DungeonRunRules;

import java.util.Set;

public record CatacombsFloorDefinition(
        CatacombsFloor floor,
        CatacombsMode mode,
        CatacombsBossEncounter boss,
        DungeonRunRules rules,
        int puzzleRooms,
        int minibossRooms,
        int trapRooms,
        Set<CatacombsPuzzle> possiblePuzzles
) {
    public CatacombsFloorDefinition {
        possiblePuzzles = Set.copyOf(possiblePuzzles);
        if (!floor.supports(mode)) {
            throw new IllegalArgumentException(floor.displayName() + " does not support " + mode);
        }
    }
}
