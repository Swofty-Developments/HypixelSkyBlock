package net.swofty.commons.bedwars;

public record LeaderboardPreferences(
		BedwarsLeaderboardPeriod period,
		BedwarsLeaderboardMode mode,
		BedwarsLeaderboardView view,
		BedwarsTextAlignment textAlignment
) {
	public static LeaderboardPreferences defaults() {
		return new LeaderboardPreferences(
				BedwarsLeaderboardPeriod.LIFETIME,
				BedwarsLeaderboardMode.ALL,
				BedwarsLeaderboardView.TOP_10,
				BedwarsTextAlignment.CENTER
		);
	}
}
