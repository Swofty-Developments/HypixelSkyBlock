package net.swofty.type.game.game.event;

import net.swofty.type.game.game.team.GameTeam;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record GameTeamWinConditionEvent<T extends GameTeam>(
	@NotNull String gameId,
	Optional<T> team
) implements GameEvent {
	@Override
	public String getGameId() {
		return gameId;
	}
}
