package net.swofty.commons.skyblock.event;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;

public enum EventNodes {

    // Overarching eventnodes
    CUSTOM(EventNode.all("custom-listener")),
    MISISON(EventNode.type("mission-listener", EventFilter.ENTITY)),
    ENTITY(EventNode.type("entity-listener", EventFilter.ENTITY)),
    PLAYER(EventNode.type("player-listener", EventFilter.PLAYER)),
    ITEM(EventNode.type("item-listener", EventFilter.PLAYER)),
    PING(EventNode.type("ping-listener", EventFilter.ALL)),
    // Player nodes

    ;

    public final EventNode<? extends Event> type;

    <E extends Event> EventNodes(EventNode<E> type) {
        this.type = type;
    }
}
