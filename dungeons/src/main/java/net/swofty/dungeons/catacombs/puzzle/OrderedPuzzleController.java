package net.swofty.dungeons.catacombs.puzzle;

import java.util.ArrayList;
import java.util.List;

public final class OrderedPuzzleController implements PuzzleController {
    private final CatacombsPuzzle puzzle;
    private final List<String> expected;
    private final List<PuzzleTrace> traces = new ArrayList<>();
    private PuzzleState state = PuzzleState.READY;
    private int index;

    public OrderedPuzzleController(CatacombsPuzzle puzzle, List<String> expected) {
        this.puzzle = puzzle;
        this.expected = List.copyOf(expected);
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
        if (!"select".equals(action.type()) || index >= expected.size() || !expected.get(index).equals(action.value())) {
            if (puzzle.failable()) {
                state = PuzzleState.FAILED;
                traces.add(PuzzleTrace.of(state, action.value()));
            }
            return;
        }
        index++;
        traces.add(PuzzleTrace.of(state, action.value()));
        if (index >= expected.size()) {
            state = PuzzleState.SOLVED;
            traces.add(PuzzleTrace.of(state, puzzle.displayName()));
        }
    }

    @Override
    public void reset() {
        index = 0;
        state = PuzzleState.RESET;
        traces.add(PuzzleTrace.of(state, "Architect's First Draft"));
    }
}
