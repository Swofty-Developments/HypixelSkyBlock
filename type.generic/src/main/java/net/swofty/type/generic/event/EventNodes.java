package net.swofty.type.generic.event;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;

public enum EventNodes {

    CUSTOM("custom-listener", EventFilter.ALL),
    MISSION("mission-listener", EventFilter.ENTITY),
    ENTITY("entity-listener", EventFilter.ENTITY),
    PLAYER("player-listener", EventFilter.PLAYER),
    PLAYER_DATA("player-data", EventFilter.PLAYER),
    ITEM("item-listener", EventFilter.PLAYER),
    PING("ping-listener", EventFilter.ALL),
    INVENTORY("inventory-listener", EventFilter.INVENTORY),
    ALL("all-listener", EventFilter.ALL);

    public final EventNode<? extends Event> eventNode;
    private final String nodeName;
    private final EventFilter<? extends Event, ?> filter;

    <E extends Event> EventNodes(String nodeName, EventFilter<E, ?> filter) {
        this.nodeName = nodeName;
        this.filter = filter;
        this.eventNode = EventNode.event(nodeName, filter, (_) -> true);
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> EventNode<E> newNode(String name, int priority) {
        return EventNode.event(nodeName + "-" + name, (EventFilter<E, ?>) filter, (_) -> true).setPriority(priority);
    }
}
