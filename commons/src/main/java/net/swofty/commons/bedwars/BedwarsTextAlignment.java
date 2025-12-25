package net.swofty.commons.bedwars;

import lombok.Getter;

@Getter
public enum BedwarsTextAlignment {
	CENTER("center", "Center"),
	BLOCK("block", "Block");

	private final String key;
	private final String displayName;

	BedwarsTextAlignment(String key, String displayName) {
		this.key = key;
		this.displayName = displayName;
	}

	public BedwarsTextAlignment next() {
		BedwarsTextAlignment[] values = values();
		int nextOrdinal = (this.ordinal() + 1) % values.length;
		return values[nextOrdinal];
	}

	public BedwarsTextAlignment previous() {
		BedwarsTextAlignment[] values = values();
		int prevOrdinal = (this.ordinal() - 1 + values.length) % values.length;
		return values[prevOrdinal];
	}

	public static BedwarsTextAlignment fromKey(String key) {
		for (BedwarsTextAlignment alignment : values()) {
			if (alignment.key.equalsIgnoreCase(key)) {
				return alignment;
			}
		}
		return CENTER;
	}
}
