package net.swofty.commons.skywars;

public record SkywarsLeaderboardPreferences(
        SkywarsLeaderboardPeriod period,
        SkywarsLeaderboardMode mode,
        SkywarsLeaderboardView view,
        SkywarsTextAlignment textAlignment
) {
    public static SkywarsLeaderboardPreferences defaults() {
        return new SkywarsLeaderboardPreferences(
                SkywarsLeaderboardPeriod.WEEKLY,
                SkywarsLeaderboardMode.ALL,
                SkywarsLeaderboardView.TOP_10,
                SkywarsTextAlignment.CENTER
        );
    }
}
