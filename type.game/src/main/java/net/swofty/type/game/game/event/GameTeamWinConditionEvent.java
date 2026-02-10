package net.swofty.type.game.game.event;

import net.swofty.type.game.game.team.GameTeam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record GameTeamWinConditionEvent<T extends GameTeam>(
	@NotNull String gameId,
	@Nullable T team
) implements GameEvent {
	@Override
	public String getGameId() {
		return gameId;
	}
}
