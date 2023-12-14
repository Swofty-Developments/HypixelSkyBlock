package net.swofty.event;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;

public enum EventNodes {

    // Overarching eventnodes
    ENTITY(EventNode.type("entity-listener", EventFilter.ENTITY)),
    PLAYER(EventNode.type("player-listener", EventFilter.PLAYER)),

    // Player nodes

    ;

    public final EventNode<? extends Event> type;

    <E extends Event> EventNodes(EventNode<E> type) {
        this.type = type;
    }
}
