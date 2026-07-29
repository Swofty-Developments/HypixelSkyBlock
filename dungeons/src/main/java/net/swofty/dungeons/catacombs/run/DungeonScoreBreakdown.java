package net.swofty.dungeons.catacombs.run;

public record DungeonScoreBreakdown(
        int skill,
        int exploration,
        int speed,
        int bonus
) {
    public int total() {
        return skill + exploration + speed + bonus;
    }

    public DungeonScoreRank rank() {
        return DungeonScoreRank.fromScore(total());
    }
}
