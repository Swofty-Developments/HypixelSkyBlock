package net.swofty.commons.bedwars;

import lombok.Getter;

@Getter
public enum BedwarsLeaderboardPeriod {
	DAILY("daily", "Daily", 1),
	WEEKLY("weekly", "Weekly", 7),
	MONTHLY("monthly", "Monthly", 30),
	LIFETIME("lifetime", "Lifetime", -1);

	private final String key;
	private final String displayName;
	private final int days;

	BedwarsLeaderboardPeriod(String key, String displayName, int days) {
		this.key = key;
		this.displayName = displayName;
		this.days = days;
	}

	public BedwarsLeaderboardPeriod next() {
		BedwarsLeaderboardPeriod[] values = values();
		int nextOrdinal = (this.ordinal() + 1) % values.length;
		return values[nextOrdinal];
	}

	public BedwarsLeaderboardPeriod previous() {
		BedwarsLeaderboardPeriod[] values = values();
		int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
		return values[prevOrdinal];
	}

	public static BedwarsLeaderboardPeriod fromKey(String key) {
		for (BedwarsLeaderboardPeriod period : values()) {
			if (period.key.equalsIgnoreCase(key)) {
				return period;
			}
		}
		return LIFETIME;
	}
}
