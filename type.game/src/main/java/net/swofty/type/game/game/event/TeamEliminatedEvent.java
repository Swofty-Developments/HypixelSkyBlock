package net.swofty.type.game.game.event;

import net.swofty.type.game.game.AbstractTeamGame;
import net.swofty.type.game.game.team.GameTeam;

public record TeamEliminatedEvent<T extends GameTeam>(
        AbstractTeamGame<?, T> game,
        T team
) implements GameEvent {
    @Override
    public String getGameId() {
        return game.getGameId();
    }
}
