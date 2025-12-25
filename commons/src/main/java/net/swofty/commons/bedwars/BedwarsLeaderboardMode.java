package net.swofty.commons.bedwars;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum BedwarsLeaderboardMode {
	ALL("all", "All Modes"),
	CORE("core", "Core Modes"),
	SOLO("solo", "Solo"),
	DOUBLES("doubles", "Doubles"),
	THREE_THREE("3v3v3v3", "3v3v3v3"),
	FOUR_FOUR_FOUR("4v4v4v4", "4v4v4v4"),
	FOUR_FOUR("4v4", "4v4");

	private final String key;
	private final String displayName;

	BedwarsLeaderboardMode(String key, String displayName) {
		this.key = key;
		this.displayName = displayName;
	}

	private static final List<BedwarsGameType> CORE_MODES = Arrays.asList(
			BedwarsGameType.SOLO,
			BedwarsGameType.DOUBLES,
			BedwarsGameType.THREE_THREE_THREE_THREE,
			BedwarsGameType.FOUR_FOUR_FOUR_FOUR
	);

	public boolean includes(BedwarsGameType gameType) {
		return switch (this) {
			case ALL -> true;
			case CORE -> CORE_MODES.contains(gameType);
			case SOLO -> gameType == BedwarsGameType.SOLO;
			case DOUBLES -> gameType == BedwarsGameType.DOUBLES;
			case THREE_THREE -> gameType == BedwarsGameType.THREE_THREE_THREE_THREE;
			case FOUR_FOUR_FOUR -> gameType == BedwarsGameType.FOUR_FOUR_FOUR_FOUR;
			case FOUR_FOUR -> gameType == BedwarsGameType.FOUR_FOUR;
		};
	}

	public BedwarsLeaderboardMode next() {
		BedwarsLeaderboardMode[] values = values();
		int nextOrdinal = (this.ordinal() + 1) % values.length;
		return values[nextOrdinal];
	}

	public BedwarsLeaderboardMode previous() {
		BedwarsLeaderboardMode[] values = values();
		int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
		return values[prevOrdinal];
	}

	public static BedwarsLeaderboardMode fromKey(String key) {
		for (BedwarsLeaderboardMode mode : values()) {
			if (mode.key.equalsIgnoreCase(key)) {
				return mode;
			}
		}
		return ALL;
	}

	public static BedwarsLeaderboardMode fromGameType(BedwarsGameType gameType) {
		return switch (gameType) {
			case SOLO -> SOLO;
			case DOUBLES, ULTIMATE_DOUBLES -> DOUBLES;
			case THREE_THREE_THREE_THREE -> THREE_THREE;
			case FOUR_FOUR_FOUR_FOUR, ULTIMATE_FOURS -> FOUR_FOUR_FOUR;
			case FOUR_FOUR -> FOUR_FOUR;
		};
	}
}
