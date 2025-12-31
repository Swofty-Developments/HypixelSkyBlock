package net.swofty.commons.murdermystery;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum MurderMysteryGameType {
	CLASSIC(0, "Classic", 12, 1, 1),
	DOUBLE_UP(1, "Double Up", 16, 2, 2),
	ASSASSINS(2, "Assassins", 12, 0, 0);

	private final int id;
	private final String displayName;
	private final int maxPlayers;
	private final int murdererCount;
	private final int detectiveCount;

	MurderMysteryGameType(int id, String displayName, int maxPlayers, int murdererCount, int detectiveCount) {
		this.id = id;
		this.displayName = displayName;
		this.maxPlayers = maxPlayers;
		this.murdererCount = murdererCount;
		this.detectiveCount = detectiveCount;
	}

	public int getMinPlayers() {
		return Math.max(4, maxPlayers / 2);
	}

	@Nullable
	public static MurderMysteryGameType from(String field) {
		for (MurderMysteryGameType type : values()) {
			if (type.name().equalsIgnoreCase(field)) {
				return type;
			}
		}
		return null;
	}

	@Nullable
	public static MurderMysteryGameType fromDisplayName(String displayName) {
		for (MurderMysteryGameType type : values()) {
			if (type.displayName.equalsIgnoreCase(displayName)) {
				return type;
			}
		}
		return null;
	}

	@Nullable
	public static MurderMysteryGameType fromId(int id) {
		for (MurderMysteryGameType type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}

	public boolean isAssassins() {
		return this == ASSASSINS;
	}
}
