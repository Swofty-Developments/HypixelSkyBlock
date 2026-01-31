package net.swofty.commons.game.event;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.UUID;

/**
 * Event fired when a player attempts to join a game.
 * Can be cancelled to prevent the join.
 */
public class PlayerJoinGameEvent implements GameEvent, CancellableEvent {
    private final String gameId;
    @Getter
	private final UUID playerId;
    @Getter
	private final String playerName;
    private boolean cancelled = false;
    @Getter
	private String cancelReason = null;

    public PlayerJoinGameEvent(String gameId, UUID playerId, String playerName) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

	@Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public void cancel(String reason) {
        this.cancelled = true;
        this.cancelReason = reason;
    }

}
