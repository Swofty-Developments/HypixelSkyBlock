package net.swofty.commons.bedwars;

import lombok.Getter;

@Getter
public enum BedwarsStatType {
	LEVEL("level", "Bed Wars Level", false),
	WINS("wins", "Wins", true),
	FINAL_KILLS("final_kills", "Final Kills", true),
	BEDS_BROKEN("beds_broken", "Beds Broken", true);

	private final String key;
	private final String displayName;
	private final boolean timedTracking;

	BedwarsStatType(String key, String displayName, boolean timedTracking) {
		this.key = key;
		this.displayName = displayName;
		this.timedTracking = timedTracking;
	}

	public static BedwarsStatType fromKey(String key) {
		for (BedwarsStatType type : values()) {
			if (type.key.equalsIgnoreCase(key)) {
				return type;
			}
		}
		return null;
	}
}
