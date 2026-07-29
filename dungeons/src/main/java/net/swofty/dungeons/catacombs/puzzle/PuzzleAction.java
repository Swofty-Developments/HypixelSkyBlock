package net.swofty.dungeons.catacombs.puzzle;

public record PuzzleAction(String type, String value) {
    public static PuzzleAction of(String type, String value) {
        return new PuzzleAction(type, value);
    }
}
