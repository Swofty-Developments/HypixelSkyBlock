package net.swofty.dungeons.catacombs.puzzle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsPuzzleFactory {
    public static PuzzleController create(CatacombsPuzzle puzzle) {
        return switch (puzzle) {
            case CREEPER_BEAMS -> new ObjectivePuzzleController(puzzle, "beam", 4);
            case WATER_BOARD -> new ObjectivePuzzleController(puzzle, "gate", 5);
            case TELEPORT_MAZE -> new ObjectivePuzzleController(puzzle, "finish", 1);
            case ICE_FILL -> new ObjectivePuzzleController(puzzle, "layer", 3);
            case ICE_PATH -> new ObjectivePuzzleController(puzzle, "detonate", 1);
            case THREE_WEIRDOS -> new OrderedPuzzleController(puzzle, List.of("correct_chest"));
            case TIC_TAC_TOE -> new OrderedPuzzleController(puzzle, List.of("tie"));
            case HIGHER_OR_LOWER -> new OrderedPuzzleController(puzzle,
                    List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
            case BOULDER -> new ObjectivePuzzleController(puzzle, "path_open", 1);
            case QUIZ -> new OrderedPuzzleController(puzzle, List.of("question_1", "question_2", "question_3"));
        };
    }
}
