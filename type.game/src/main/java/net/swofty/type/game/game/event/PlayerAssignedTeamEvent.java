package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;

public record PlayerAssignedTeamEvent<T>(
        String gameId,
        Player player,
        T team
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
