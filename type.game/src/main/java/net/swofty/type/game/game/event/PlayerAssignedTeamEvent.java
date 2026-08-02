package net.swofty.type.game.game.event;

import net.minestom.server.entity.Player;
import net.swofty.type.game.game.Game;

public record PlayerAssignedTeamEvent<T>(
        Game<?> game,
        Player player,
        T team
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
