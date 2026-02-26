package net.swofty.type.generic.event;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;

public enum EventNodes {

    // Overarching eventnodes
    CUSTOM(EventNode.all("custom-listener")),
    MISSION(EventNode.type("mission-listener", EventFilter.ENTITY)),
    ENTITY(EventNode.type("entity-listener", EventFilter.ENTITY)),
    PLAYER(EventNode.type("player-listener", EventFilter.PLAYER).setPriority(2)),
    PLAYER_DATA(EventNode.type("player-data", EventFilter.PLAYER).setPriority(1)),
    ITEM(EventNode.type("item-listener", EventFilter.PLAYER)),
    PING(EventNode.type("ping-listener", EventFilter.ALL)),
    INVENTORY(EventNode.type("inventory-listener", EventFilter.INVENTORY)),
    ALL(EventNode.all("all-listener")),
    // Player nodes

    ;

    public final EventNode<? extends Event> eventNode;

    <E extends Event> EventNodes(EventNode<E> eventNode) {
        this.eventNode = eventNode;
    }
}
