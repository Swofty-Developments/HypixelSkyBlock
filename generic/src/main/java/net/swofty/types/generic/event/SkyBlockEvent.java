package net.swofty.types.generic.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.mission.SkyBlockMission;
import net.swofty.types.generic.user.PlayerHookManager;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SkyBlockEvent {
    private static final HashMap<EventNode<? extends Event>, ArrayList<SkyBlockEvent>> cachedEvents = new HashMap<>();
    private static final ArrayList<SkyBlockEvent> cachedCustomEvents = new ArrayList<>();
    private static final EventNode<Event> customEventNode = (EventNode<Event>) EventNodes.CUSTOM.type;

    private final EventParameters params;

    protected SkyBlockEvent() {
        this.params = this.getClass().getAnnotation(EventParameters.class);
    }

    public abstract Class<? extends net.minestom.server.event.Event> getEvent();

    public abstract void run(Event tempEvent);

    public void cacheEvent() {
        EventNodes paramNode;
        try {
            paramNode = params.node();
        } catch (NullPointerException ex) {
            if (this instanceof SkyBlockMission mission) {
                if (mission.hasNoEvent()) return;
            }
            System.out.println("Event " + this.getClass().getSimpleName() + " has no node specified!");
            return;
        }

        if (paramNode == EventNodes.CUSTOM) {
            cachedCustomEvents.add(this);
            return;
        }

        if (cachedEvents.containsKey(paramNode.type)) {
            ArrayList<SkyBlockEvent> preExisting = cachedEvents.get(paramNode.type);
            preExisting.add(this);
            cachedEvents.put(paramNode.type, preExisting);
        } else {
            ArrayList<SkyBlockEvent> preExisting = new ArrayList<>();
            preExisting.add(this);
            cachedEvents.put(paramNode.type, preExisting);
        }
    }

    public static void register(GlobalEventHandler eventHandler) {
        cachedCustomEvents.forEach(skyBlockEvent -> {
            customEventNode.addListener(skyBlockEvent.getEvent(), skyBlockEvent::run);
        });

        cachedEvents.forEach((eventNode, skyBlockEvents) -> {
            @SuppressWarnings("unchecked")
            // Here we use a raw type to bypass the generics system
            EventNode<Event> typeErasedNode = (EventNode<Event>) eventNode;

            skyBlockEvents.forEach(skyBlockEvent -> {
                // This will get the specific Class object of the event type that SkyBlockEvent works with
                Class<? extends Event> eventType = skyBlockEvent.getEvent();

                // Now we add the listener to the raw event node using the specific event type
                typeErasedNode.addListener(eventType, rawEvent -> {
                    // This unchecked cast is necessary due to type erasure -- it should be safe as long as
                    // getEvent() returns the correct type for this SkyBlockEvent.
                    @SuppressWarnings("unchecked")
                    Event concreteEvent = eventType.cast(rawEvent);

                    if (concreteEvent instanceof PlayerEvent event
                            && skyBlockEvent.params.requireDataLoaded()
                            && (DataHandler.getUser(event.getPlayer()) == null
                                || (SkyBlockConst.isIslandServer() &&
                                    !((SkyBlockPlayer) event.getPlayer()).getSkyBlockIsland().getCreated()))) {
                        Scheduler scheduler = MinecraftServer.getSchedulerManager();

                        scheduler.submitTask(() -> {
                            Player player = ((PlayerEvent) concreteEvent).getPlayer();
                            if (!player.isOnline()) return TaskSchedule.stop();
                            if (DataHandler.getUser(player) == null) return TaskSchedule.millis(2);
                            if (SkyBlockConst.isIslandServer() &&
                                    !((SkyBlockPlayer) player).getSkyBlockIsland().getCreated()) return TaskSchedule.millis(2);

                            runEvent(skyBlockEvent, concreteEvent);
                            return TaskSchedule.stop();
                        });
                    } else {
                        // Now run the event with the properly casted type
                        try {
                            runEvent(skyBlockEvent, concreteEvent);
                        } catch (Exception ex) {
                            Logger.info("Exception occurred while running event " + skyBlockEvent.getClass().getSimpleName() + " with event type " + concreteEvent.getClass().getSimpleName());
                            if (skyBlockEvent instanceof EventException exceptionEvent) {
                                exceptionEvent.onException(ex, concreteEvent);
                                return;
                            }
                            ex.printStackTrace();
                        }
                    }
                });
            });

            eventHandler.addChild(typeErasedNode);
        });
    }

    private static void runEvent(SkyBlockEvent event, Event concreteEvent) {
        PlayerHookManager hookManager = null;

        if (concreteEvent instanceof PlayerEvent)
            hookManager = ((SkyBlockPlayer) (((PlayerEvent) concreteEvent).getPlayer())).getHookManager();

        if (hookManager != null)
            hookManager.callAndClearHooks(
                    event, true);

        if (event.params.isAsync()) {
            Thread.startVirtualThread(() -> event.run(concreteEvent));
        } else {
            event.run(concreteEvent);
        }

        if (hookManager != null)
            hookManager.callAndClearHooks(
                    event, false);
    }

    public static void callSkyBlockEvent(Event event) {
        if (customEventNode != null) {
            if (event instanceof PlayerEvent playerEvent) {
                if (DataHandler.getUser(playerEvent.getPlayer()) == null) return;
            }
            customEventNode.call(event);
        }
    }
}
