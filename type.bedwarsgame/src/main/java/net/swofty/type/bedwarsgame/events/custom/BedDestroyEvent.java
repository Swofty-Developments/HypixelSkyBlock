package net.swofty.type.bedwarsgame.events.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.game.event.GameEvent;

import java.util.UUID;

@Getter
public class BedDestroyEvent implements GameEvent, CancellableEvent {
    private final String gameId;
    private final TeamKey teamKey;
    private final UUID destroyerId;
    private final String destroyerName;
    private boolean cancelled = false;

    public BedDestroyEvent(String gameId, TeamKey teamKey, UUID destroyerId, String destroyerName) {
        this.gameId = gameId;
        this.teamKey = teamKey;
        this.destroyerId = destroyerId;
        this.destroyerName = destroyerName;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
