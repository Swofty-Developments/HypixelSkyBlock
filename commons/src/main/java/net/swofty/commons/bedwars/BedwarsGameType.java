package net.swofty.commons.bedwars;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum BedwarsGameType {
	SOLO(0, "Solo", 1, 8),
	DOUBLES(1, "Doubles", 2, 8),
	THREE_THREE_THREE_THREE(2, "3v3v3v3", 3, 4),
	FOUR_FOUR_FOUR_FOUR(3, "4v4v4v4", 4, 4),
	FOUR_FOUR(4, "4v4", 4, 2),
	ULTIMATE_DOUBLES(5, "Ultimate", 2, 8),
	ULTIMATE_FOURS(6, "Ultimate", 4, 4);

	private final int id;
	private final String displayName;
	private final int teamSize;
	private final int teams;

	BedwarsGameType(int id, String displayName, int teamSize, int teams) {
		this.id = id;
		this.displayName = displayName;
		this.teamSize = teamSize;
		this.teams = teams;
	}

	public int maxPlayers() {
		return teamSize * teams;
	}

	@Nullable
	public static BedwarsGameType from(String field) {
		for (BedwarsGameType type : values()) {
			if (type.name().equalsIgnoreCase(field)) {
				return type;
			}
		}
		return null;
	}

	@Nullable
	public static BedwarsGameType fromDisplayName(String displayName) {
		for (BedwarsGameType type : values()) {
			if (type.displayName.equalsIgnoreCase(displayName)) {
				return type;
			}
		}
		return null;
	}

	@Nullable
	public static BedwarsGameType fromId(int id) {
		for (BedwarsGameType type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}

	public boolean isDoublesSolo() {
		return this == SOLO || this == DOUBLES || this == ULTIMATE_DOUBLES;
	}
}
