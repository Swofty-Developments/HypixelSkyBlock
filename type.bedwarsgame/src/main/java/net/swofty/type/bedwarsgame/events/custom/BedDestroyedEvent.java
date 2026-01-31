package net.swofty.type.bedwarsgame.events.custom;

import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.game.event.GameEvent;

import java.util.UUID;

public record BedDestroyedEvent(
        String gameId,
        TeamKey teamKey,
        UUID destroyerId,
        String destroyerName
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
