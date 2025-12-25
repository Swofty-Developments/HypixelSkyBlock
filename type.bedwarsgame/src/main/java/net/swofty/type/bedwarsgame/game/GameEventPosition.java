package net.swofty.type.bedwarsgame.game;

import lombok.Getter;

@Getter
public enum GameEventPosition {
	BEGIN(30, 65),
	DIAMOND_2(6 * 60, 30, 65),
	EMERALD_2(6 * 60, 30, 65),
	DIAMOND_3(6 * 60, 30, 65),
	EMERALD_3(6 * 60, 30, 65),
	BED_BREAK(6 * 60, 30, 65),
	SUDDEN_DEATH(10 * 60, 30, 65),
	GAME_END(10 * 60, 30, 65);

	private final long time; // in seconds
	private final long diamondSeconds;
	private final long emeraldSeconds;

	GameEventPosition(long diamondSeconds, long emeraldSeconds) {
		this.time = 0;
		this.diamondSeconds = diamondSeconds;
		this.emeraldSeconds = emeraldSeconds;
	}

	GameEventPosition(long time, long diamondSeconds, long emeraldSeconds) {
		this.time = time;
		this.diamondSeconds = diamondSeconds;
		this.emeraldSeconds = emeraldSeconds;
	}

	public String getDisplayName() {
		return switch (this) {
			case BEGIN -> "Game Start";
			case DIAMOND_2 -> "Diamond II";
			case EMERALD_2 -> "Emerald II";
			case DIAMOND_3 -> "Diamond III";
			case EMERALD_3 -> "Emerald III";
			case BED_BREAK -> "Bed Break";
			case SUDDEN_DEATH -> "Sudden Death";
			case GAME_END -> "Game End";
		};
	}

}
