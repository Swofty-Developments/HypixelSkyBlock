package net.swofty.types.generic.event;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.user.PlayerHookManager;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyBlockEventHandler {
    private static final HashMap<Class<?>, List<EventMethodEntry>> cachedEvents = new HashMap<>();
    private static final ArrayList<EventMethodEntry> cachedCustomEvents = new ArrayList<>();
    private static final EventNode<Event> customEventNode = (EventNode<Event>) EventNodes.CUSTOM.eventNode;

    private record EventMethodEntry(Method method, Object instance, SkyBlockEvent skyBlockEvent) { }

    public static void registerEventMethods(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(SkyBlockEvent.class)) {
                SkyBlockEvent skyBlockEvent = method.getAnnotation(SkyBlockEvent.class);
                EventNodes paramNode = skyBlockEvent.node();

                if (paramNode == EventNodes.CUSTOM) {
                    if (cachedCustomEvents.contains(new EventMethodEntry(method, instance, skyBlockEvent))) {
                        return;
                    }
                    cachedCustomEvents.add(new EventMethodEntry(method, instance, skyBlockEvent));
                    return;
                }

                if (!cachedEvents.containsKey(method.getParameterTypes()[0])) {
                    cachedEvents.put(method.getParameterTypes()[0], new ArrayList<>());
                }
                cachedEvents.get(method.getParameterTypes()[0]).add(new EventMethodEntry(method, instance, skyBlockEvent));
            }
        }
    }

    public static void register(GlobalEventHandler eventHandler) {
        cachedCustomEvents.forEach(skyBlockEvent -> {
            try {
                Class<? extends Event> eventType = (Class<? extends Event>) skyBlockEvent.method.getParameterTypes()[0];
                customEventNode.addListener(eventType, (event) -> {
                    try {
                        skyBlockEvent.method.invoke(skyBlockEvent.instance, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                Logger.error("Error occurred while registering custom event " + skyBlockEvent.getClass().getSimpleName());
                e.printStackTrace();
            }
        });

        eventHandler.addChild(customEventNode);

        Map<EventNode<? extends Event>, List<EventMethodEntry>> eventNodes = new HashMap<>();
        cachedEvents.forEach((eventTypeUncasted, methodPair) -> {
            Class<? extends Event> eventType = (Class<? extends Event>) eventTypeUncasted;

            methodPair.forEach(eventMethod -> {
                EventNodes paramNode = eventMethod.skyBlockEvent().node();

                ((EventNode<Event>) paramNode.eventNode).addListener(eventType, rawEvent -> {
                    Event concreteEvent = eventType.cast(rawEvent);

                    if (concreteEvent instanceof PlayerEvent event
                            && eventMethod.skyBlockEvent().requireDataLoaded()
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

                            runEvent(eventMethod.skyBlockEvent(), eventMethod.method, eventMethod.instance, concreteEvent);
                            return TaskSchedule.stop();
                        });
                    } else {
                        // Now run the event with the properly casted type
                        try {
                            runEvent(eventMethod.skyBlockEvent(), eventMethod.method, eventMethod.instance, concreteEvent);
                        } catch (Exception ex) {
                            Logger.info("Exception occurred while running event " +
                                    eventMethod.method.getClass().getSimpleName() + " with event type " +
                                    concreteEvent.getClass().getSimpleName());
                            ex.printStackTrace();
                        }
                    }
                });

                if (eventNodes.containsKey(paramNode.eventNode)) {
                    eventNodes.get(paramNode.eventNode).add(eventMethod);
                } else {
                    eventNodes.put(paramNode.eventNode, new ArrayList<>());
                    eventNodes.get(paramNode.eventNode).add(eventMethod);
                }
            });
        });

        for (EventNode<? extends Event> paramNode : eventNodes.keySet()) {
            eventHandler.addChild(paramNode);
        }
    }

    @SneakyThrows
    private static void runEvent(SkyBlockEvent event, Method method, Object eventClassInstance, Event concreteEvent) {
        PlayerHookManager hookManager = null;

        if (concreteEvent instanceof PlayerEvent)
            hookManager = ((SkyBlockPlayer) (((PlayerEvent) concreteEvent).getPlayer())).getHookManager();
        if (concreteEvent instanceof PlayerInstanceEvent)
            hookManager = ((SkyBlockPlayer) (((PlayerInstanceEvent) concreteEvent).getPlayer())).getHookManager();

        if (hookManager != null)
            hookManager.callAndClearHooks(
                    eventClassInstance.getClass(), true);

        if (event.isAsync()) {
            PlayerHookManager finalHookManager = hookManager;
            Thread.startVirtualThread(() -> {
                try {
                    method.invoke(eventClassInstance, concreteEvent);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                if (finalHookManager != null)
                    finalHookManager.callAndClearHooks(
                            eventClassInstance.getClass(), false);
            });
        } else {
            method.invoke(eventClassInstance, concreteEvent);

            if (hookManager != null)
                hookManager.callAndClearHooks(
                        eventClassInstance.getClass(), false);
        }
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
