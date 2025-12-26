package net.swofty.commons.bedwars;

import lombok.Getter;

@Getter
public enum BedwarsLeaderboardView {
	TOP_10("top_10", "Top 10"),
	PLAYERS_AROUND_YOU("around_you", "Players Around You");

	private final String key;
	private final String displayName;

	BedwarsLeaderboardView(String key, String displayName) {
		this.key = key;
		this.displayName = displayName;
	}

	public BedwarsLeaderboardView next() {
		BedwarsLeaderboardView[] values = values();
		int nextOrdinal = (this.ordinal() + 1) % values.length;
		return values[nextOrdinal];
	}

	public BedwarsLeaderboardView previous() {
		BedwarsLeaderboardView[] values = values();
		int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
		return values[prevOrdinal];
	}

	public static BedwarsLeaderboardView fromKey(String key) {
		for (BedwarsLeaderboardView view : values()) {
			if (view.key.equalsIgnoreCase(key)) {
				return view;
			}
		}
		return TOP_10;
	}
}
