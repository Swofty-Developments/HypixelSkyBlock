package net.swofty.type.bedwarsgame.game;

import lombok.Getter;

@Getter
public enum GameEventPosition {
	BEGIN,
	DIAMOND_2(6 * 60),
	EMERALD_2(6 * 60),
	DIAMOND_3(6 * 60),
	EMERALD_3(6 * 60),
	BED_BREAK(6 * 60),
	SUDDEN_DEATH(10 * 60),
	GAME_END(10 * 60);

	private long time;

	GameEventPosition() {
		this.time = 0;
	}

	GameEventPosition(long time) {
		this.time = time;
	}

}
