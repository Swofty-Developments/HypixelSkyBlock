package net.swofty.type.game.game.event;

import net.minestom.server.event.Event;
import net.swofty.type.game.game.Game;

public interface GameEvent extends Event {
    Game<?> getGame();
}
