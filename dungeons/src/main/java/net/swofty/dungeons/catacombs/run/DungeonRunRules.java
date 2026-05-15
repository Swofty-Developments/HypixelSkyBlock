package net.swofty.dungeons.catacombs.run;

public record DungeonRunRules(
        int speedScoreSeconds,
        int maximumDurationSeconds,
        int minimumPartySize,
        int maximumPartySize,
        int bonusScoreCap,
        boolean automaticGhostRevive,
        boolean trapRoomsEnabled
) {
    public boolean allowsPartySize(int size) {
        return size >= minimumPartySize && size <= maximumPartySize;
    }
}
