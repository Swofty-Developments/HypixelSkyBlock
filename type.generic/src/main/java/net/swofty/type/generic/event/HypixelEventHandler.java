package net.swofty.type.generic.event;

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
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.PlayerHookManager;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HypixelEventHandler {
    private static final HashMap<Class<?>, List<EventMethodEntry>> cachedEvents = new HashMap<>();
    private static final ArrayList<EventMethodEntry> cachedCustomEvents = new ArrayList<>();
    private static final EventNode<Event> customEventNode = (EventNode<Event>) EventNodes.CUSTOM.eventNode;

    private record EventMethodEntry(Method method,
                                    Object instance,
                                    HypixelEvent hypixelEvent) { }

    public static void registerEventMethods(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(HypixelEvent.class)) {
                HypixelEvent hypixelEvent = method.getAnnotation(HypixelEvent.class);
                EventNodes paramNode = hypixelEvent.node();

                if (paramNode == EventNodes.CUSTOM) {
                    if (cachedCustomEvents.contains(new EventMethodEntry(method, instance, hypixelEvent))) {
                        continue;
                    }
                    cachedCustomEvents.add(new EventMethodEntry(method, instance, hypixelEvent));
                    continue;
                }

                if (!cachedEvents.containsKey(method.getParameterTypes()[0])) {
                    cachedEvents.put(method.getParameterTypes()[0], new ArrayList<>());
                }
                cachedEvents.get(method.getParameterTypes()[0]).add(new EventMethodEntry(method, instance, hypixelEvent));
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
                Logger.error(e, "Error occurred while registering custom event: {}", skyBlockEvent.getClass().getSimpleName());
            }
        });

        eventHandler.addChild(customEventNode);

        Map<EventNode<? extends Event>, List<EventMethodEntry>> eventNodes = new HashMap<>();
        cachedEvents.forEach((eventTypeUncasted, methodPair) -> {
            Class<? extends Event> eventType = (Class<? extends Event>) eventTypeUncasted;

            methodPair.forEach(eventMethod -> {
                EventNodes paramNode = eventMethod.hypixelEvent().node();

                ((EventNode<Event>) paramNode.eventNode).addListener(eventType, rawEvent -> {
                    Event concreteEvent = eventType.cast(rawEvent);

                    if (concreteEvent instanceof PlayerEvent event
                            && eventMethod.hypixelEvent().requireDataLoaded()
                            && (HypixelDataHandler.getUser(event.getPlayer()) == null
                                || (HypixelConst.isIslandServer() &&
                                    !((HypixelPlayer) event.getPlayer()).isReadyForEvents()))) {
                        Scheduler scheduler = MinecraftServer.getSchedulerManager();

                        scheduler.submitTask(() -> {
                            Player player = event.getPlayer();
                            if (!player.isOnline()) return TaskSchedule.stop();
                            if (HypixelDataHandler.getUser(player) == null) return TaskSchedule.millis(2);
                            if (HypixelConst.isIslandServer() &&
                                    !((HypixelPlayer) player).isReadyForEvents()) return TaskSchedule.millis(2);

                            runEvent(eventMethod.hypixelEvent(), eventMethod.method, eventMethod.instance, concreteEvent);
                            return TaskSchedule.stop();
                        });
                    } else {
                        // Now run the event with the properly cast type
                        try {
                            runEvent(eventMethod.hypixelEvent(), eventMethod.method, eventMethod.instance, concreteEvent);
                        } catch (Exception ex) {
                            Logger.error(ex, "Exception occurred while running event {} with event type {}",
                                    eventMethod.method.getClass().getSimpleName(),
                                    concreteEvent.getClass().getSimpleName());
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
    private static void runEvent(HypixelEvent event, Method method, Object eventClassInstance, Event concreteEvent) {
        PlayerHookManager hookManager = null;

        if (concreteEvent instanceof PlayerEvent)
            hookManager = ((HypixelPlayer) (((PlayerEvent) concreteEvent).getPlayer())).getHookManager();
        if (concreteEvent instanceof PlayerInstanceEvent)
            hookManager = ((HypixelPlayer) (((PlayerInstanceEvent) concreteEvent).getPlayer())).getHookManager();

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

    public static void callCustomEvent(Event event) {
        if (customEventNode != null) {
            if (event instanceof PlayerEvent playerEvent) {
                if (HypixelDataHandler.getUser(playerEvent.getPlayer()) == null) {
                    Logger.warn("Tried to call custom event {} for player {} but their data is not loaded.",
                            event.getClass().getSimpleName(),
                            playerEvent.getPlayer().getUsername());
                    return;
                }
            }
            customEventNode.call(event);
        }
    }
}
