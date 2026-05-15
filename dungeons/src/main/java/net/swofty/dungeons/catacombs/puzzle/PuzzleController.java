package net.swofty.dungeons.catacombs.puzzle;

import java.util.List;

public interface PuzzleController {
    CatacombsPuzzle puzzle();

    PuzzleState state();

    List<PuzzleTrace> traces();

    void accept(PuzzleAction action);

    void reset();
}
