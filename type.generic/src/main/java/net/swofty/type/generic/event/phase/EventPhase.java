package net.swofty.type.generic.event.phase;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public enum EventPhase {
    CONNECT(10),
    LOAD_DATA(20),
    POST_DATA(30),
    SPAWN(40),
    POST_SPAWN(50),
    GAMEPLAY(60),
    PERSIST(80),
    DISCONNECT(90);

    private final int priority;
    private final EventNode<Event> node;

    EventPhase(int priority) {
        this.priority = priority;
        this.node = EventNode.all("phase-" + name().toLowerCase()).setPriority(priority);
    }

    public int priority() {
        return priority;
    }

    public EventNode<Event> node() {
        return node;
    }
}
