package net.swofty.type.bedwarsgeneric.game;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum GameType {
	SOLO(0, "Solo", 1, 8),
	DOUBLES(1, "Doubles", 2, 16),
	THREE_THREE_THREE_THREE(2, "3v3v3v3", 3, 12),
	FOUR_FOUR_FOUR_FOUR(3, "4v4v4v4", 4, 16),
	FOUR_FOUR(4, "4v4", 4, 8),
	ULTIMATE_DOUBLES(5, "Ultimate", 2, 16),
	ULTIMATE_FOURS(6, "Ultimate", 4, 16);

	private final int id;
	private final String displayName;
	private final int teamSize;
	private final int maxPlayers;

	GameType(int id, String displayName, int teamSize, int maxPlayers) {
		this.id = id;
		this.displayName = displayName;
		this.teamSize = teamSize;
		this.maxPlayers = maxPlayers;
	}

	@Nullable
	public static GameType from(String field) {
		for (GameType type : values()) {
			if (type.name().equalsIgnoreCase(field)) {
				return type;
			}
		}
		return null;
	}

	@Nullable
	public static GameType fromDisplayName(String displayName) {
		for (GameType type : values()) {
			if (type.displayName.equalsIgnoreCase(displayName)) {
				return type;
			}
		}
		return null;
	}

	@Nullable
	public static GameType fromId(int id) {
		for (GameType type : values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}
}
