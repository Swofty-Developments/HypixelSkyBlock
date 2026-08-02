package net.swofty.type.bedwarsgame.events.custom;

import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.Game;
import net.swofty.type.game.game.event.GameEvent;

public record BedDestroyedEvent(
        Game<?> game,
    TeamKey teamKey,
    BedWarsPlayer destroyer
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
