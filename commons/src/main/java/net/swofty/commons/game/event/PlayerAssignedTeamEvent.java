package net.swofty.commons.game.event;

import java.util.UUID;

public record PlayerAssignedTeamEvent(
        String gameId,
        UUID playerId,
        String playerName,
        String teamId,
        String teamName
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
