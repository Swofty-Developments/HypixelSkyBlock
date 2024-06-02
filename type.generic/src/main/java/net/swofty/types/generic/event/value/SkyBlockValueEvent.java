package net.swofty.types.generic.event.value;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.ArrayList;

public abstract class SkyBlockValueEvent {
    private static final ArrayList<SkyBlockValueEvent> cachedCustomEvents = new ArrayList<>();
    private static final EventNode<Event> customEventNode = EventNode.all("value-listener");

    public abstract Class<? extends ValueUpdateEvent> getValueEvent();

    public abstract void run(ValueUpdateEvent tempEvent);

    public void cacheEvent() {
        cachedCustomEvents.add(this);
    }

    public static void register() {
        cachedCustomEvents.forEach(eventClass -> {
            Class<? extends ValueUpdateEvent> clazz = eventClass.getValueEvent();

            customEventNode.addListener(clazz, eventClass::run);
        });
    }

    public static void callValueUpdateEvent(ValueUpdateEvent event) {
        customEventNode.call(event);
    }
}
