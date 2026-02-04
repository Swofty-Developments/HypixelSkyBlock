package net.swofty.commons.zombies;

public record ZombiesLeaderboardPreferences(
        ZombiesLeaderboardPeriod period,
        ZombiesLeaderboardMode mode,
        ZombiesLeaderboardView view,
        ZombiesTextAlignment textAlignment
) {
    public static ZombiesLeaderboardPreferences defaults() {
        return new ZombiesLeaderboardPreferences(
                ZombiesLeaderboardPeriod.WEEKLY,
                ZombiesLeaderboardMode.ALL,
                ZombiesLeaderboardView.TOP_10,
                ZombiesTextAlignment.CENTER
        );
    }
}
