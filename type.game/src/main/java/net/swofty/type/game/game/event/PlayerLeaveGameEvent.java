package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;

public record PlayerLeaveGameEvent(
        String gameId,
        Player player,
        LeaveReason reason
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }

    public enum LeaveReason {
        VOLUNTARY,
        KICKED,
        ELIMINATED,
        GAME_END
    }
}
