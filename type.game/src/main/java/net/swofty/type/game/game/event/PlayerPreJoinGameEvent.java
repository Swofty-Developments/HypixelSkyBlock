package net.swofty.type.game.game.event;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;

/**
 * Event fired when a player attempts to join a game.
 * Can be cancelled to prevent the join.
 */
public class PlayerPreJoinGameEvent implements GameEvent, PlayerEvent, CancellableEvent {
    private final String gameId;
    @Getter
    private final Player player;

    private boolean cancelled = false;
    @Getter
    private String cancelReason = null;

    public PlayerPreJoinGameEvent(String gameId, Player player) {
        this.gameId = gameId;
        this.player = player;
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
