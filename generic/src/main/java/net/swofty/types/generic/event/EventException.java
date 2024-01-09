package net.swofty.types.generic.event;

import net.minestom.server.event.Event;

public interface EventException {
    void onException(Exception e, Event tempEvent);
}
