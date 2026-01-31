package net.swofty.commons.game.event;

import net.minestom.server.event.Event;

public interface GameEvent extends Event {
    String getGameId();
}
