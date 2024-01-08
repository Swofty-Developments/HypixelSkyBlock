package net.swofty.types.generic.event;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;

public enum EventNodes {

    // Overarching eventnodes
    CUSTOM(EventNode.all("custom-listener")),
    MISISON(EventNode.type("mission-listener", EventFilter.ENTITY)),
    ENTITY(EventNode.type("entity-listener", EventFilter.ENTITY)),
    PLAYER(EventNode.type("player-listener", EventFilter.PLAYER).setPriority(2)),
    PLAYER_DATA(EventNode.type("player-data", EventFilter.PLAYER).setPriority(1)),
    ITEM(EventNode.type("item-listener", EventFilter.PLAYER)),
    PING(EventNode.type("ping-listener", EventFilter.ALL)),
    // Player nodes

    ;

    public final EventNode<? extends Event> type;

    <E extends Event> EventNodes(EventNode<E> type) {
        this.type = type;
    }
}
