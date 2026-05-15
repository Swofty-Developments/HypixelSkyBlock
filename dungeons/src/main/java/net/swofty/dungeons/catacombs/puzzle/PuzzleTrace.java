package net.swofty.dungeons.catacombs.puzzle;

import java.time.Instant;

public record PuzzleTrace(PuzzleState state, Instant happenedAt, String detail) {
    public static PuzzleTrace of(PuzzleState state, String detail) {
        return new PuzzleTrace(state, Instant.now(), detail);
    }
}
