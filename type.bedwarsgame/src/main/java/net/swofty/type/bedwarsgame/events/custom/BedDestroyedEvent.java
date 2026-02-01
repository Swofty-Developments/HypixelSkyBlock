package net.swofty.type.bedwarsgame.events.custom;

import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.game.event.GameEvent;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

public record BedDestroyedEvent(
        String gameId,
        TeamKey teamKey,
        BedWarsPlayer destroyer
) implements GameEvent {
    @Override
    public String getGameId() {
        return gameId;
    }
}
