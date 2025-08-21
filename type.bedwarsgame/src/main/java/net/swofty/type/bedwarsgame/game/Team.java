package net.swofty.type.bedwarsgame.game;

import lombok.Data;

/**
 * WIP: Data representation of a team to pass around.
 */
@Data
public class Team {

	private final String name;
	// should handle color-related logic

	public Team(String name) {
		this.name = name;
	}

}
