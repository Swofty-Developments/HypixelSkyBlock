package net.swofty.type.game.game.event;

import net.minestom.server.event.Event;

public interface GameEvent extends Event {
    String getGameId();
}
