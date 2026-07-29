package net.swofty.dungeons.catacombs.run;

import java.util.Arrays;
import java.util.Comparator;

public enum DungeonScoreRank {
    S_PLUS("S+", 300),
    S("S", 270),
    A("A", 230),
    B("B", 160),
    C("C", 100),
    D("D", 40),
    NONE("", 0);

    private final String displayName;
    private final int minimumScore;

    DungeonScoreRank(String displayName, int minimumScore) {
        this.displayName = displayName;
        this.minimumScore = minimumScore;
    }

    public String displayName() {
        return displayName;
    }

    public int minimumScore() {
        return minimumScore;
    }

    public static DungeonScoreRank fromScore(int score) {
        return Arrays.stream(values())
                .sorted(Comparator.comparingInt(DungeonScoreRank::minimumScore).reversed())
                .filter(rank -> score >= rank.minimumScore)
                .findFirst()
                .orElse(NONE);
    }
}
