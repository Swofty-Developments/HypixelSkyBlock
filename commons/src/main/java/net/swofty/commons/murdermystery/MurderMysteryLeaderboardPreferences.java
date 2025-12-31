package net.swofty.commons.murdermystery;

public record MurderMysteryLeaderboardPreferences(
        MurderMysteryLeaderboardPeriod period,
        MurderMysteryLeaderboardMode mode,
        MurderMysteryLeaderboardView view,
        MurderMysteryTextAlignment textAlignment
) {
    public static MurderMysteryLeaderboardPreferences defaults() {
        return new MurderMysteryLeaderboardPreferences(
                MurderMysteryLeaderboardPeriod.WEEKLY,
                MurderMysteryLeaderboardMode.CLASSIC,
                MurderMysteryLeaderboardView.TOP_10,
                MurderMysteryTextAlignment.CENTER
        );
    }
}
