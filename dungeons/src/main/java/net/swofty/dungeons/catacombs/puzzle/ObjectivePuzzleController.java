package net.swofty.dungeons.catacombs.puzzle;

import java.util.ArrayList;
import java.util.List;

public final class ObjectivePuzzleController implements PuzzleController {
    private final CatacombsPuzzle puzzle;
    private final String objectiveType;
    private final int requiredProgress;
    private final List<PuzzleTrace> traces = new ArrayList<>();
    private PuzzleState state = PuzzleState.READY;
    private int progress;

    public ObjectivePuzzleController(CatacombsPuzzle puzzle, String objectiveType, int requiredProgress) {
        this.puzzle = puzzle;
        this.objectiveType = objectiveType;
        this.requiredProgress = requiredProgress;
    }

    @Override
    public CatacombsPuzzle puzzle() {
        return puzzle;
    }

    @Override
    public PuzzleState state() {
        return state;
    }

    @Override
    public List<PuzzleTrace> traces() {
        return List.copyOf(traces);
    }

    @Override
    public void accept(PuzzleAction action) {
        if (state == PuzzleState.SOLVED || state == PuzzleState.FAILED) {
            return;
        }
        state = PuzzleState.ACTIVE;
        if ("fail".equals(action.type()) && puzzle.failable()) {
            state = PuzzleState.FAILED;
            traces.add(PuzzleTrace.of(state, action.value()));
            return;
        }
        if (objectiveType.equals(action.type())) {
            progress++;
            traces.add(PuzzleTrace.of(state, action.type() + "=" + progress));
            if (progress >= requiredProgress) {
                state = PuzzleState.SOLVED;
                traces.add(PuzzleTrace.of(state, puzzle.displayName()));
            }
        }
    }

    @Override
    public void reset() {
        progress = 0;
        state = PuzzleState.RESET;
        traces.add(PuzzleTrace.of(state, "Architect's First Draft"));
    }
}
