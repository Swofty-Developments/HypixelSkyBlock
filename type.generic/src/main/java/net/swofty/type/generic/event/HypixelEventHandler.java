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
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.PlayerHookManager;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HypixelEventHandler {
    private static final ArrayList<EventMethodEntry> cachedEvents = new ArrayList<>();
    private static final EventNode<Event> rootNode = EventNode.all("hypixel-phased-events");
    private static boolean registered = false;

    private record EventMethodEntry(Method method,
                                    Object instance,
                                    EventNodes node,
                                    EventPhase phase,
                                    int order,
                                    boolean requireDataLoaded,
                                    boolean isAsync) { }

    public static void registerEventMethods(Object instance) {
        Class<?> clazz = instance.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            EventMethodEntry entry = eventMethodEntry(instance, method);
            if (entry != null && !cachedEvents.contains(entry)) {
                cachedEvents.add(entry);
            }
        }
    }

    public static void register(GlobalEventHandler eventHandler) {
        if (registered) return;

        for (EventPhase phase : EventPhase.values()) {
            rootNode.addChild(phase.node());
        }

        cachedEvents.stream()
                .sorted((first, second) -> {
                    int phaseCompare = Integer.compare(first.phase().priority(), second.phase().priority());
                    if (phaseCompare != 0) return phaseCompare;
                    return Integer.compare(first.order(), second.order());
                })
                .forEach(HypixelEventHandler::registerEntry);

        eventHandler.addChild(rootNode);
        registered = true;
    }

    @SneakyThrows
    private static void runEvent(EventMethodEntry entry, Event concreteEvent) {
        PlayerHookManager hookManager = null;

        if (concreteEvent instanceof PlayerEvent)
            hookManager = ((HypixelPlayer) (((PlayerEvent) concreteEvent).getPlayer())).getHookManager();
        if (concreteEvent instanceof PlayerInstanceEvent)
            hookManager = ((HypixelPlayer) (((PlayerInstanceEvent) concreteEvent).getPlayer())).getHookManager();

        if (hookManager != null)
            hookManager.callAndClearHooks(
                    entry.instance().getClass(), true);

        if (entry.isAsync()) {
            PlayerHookManager finalHookManager = hookManager;
            Thread.startVirtualThread(() -> {
                try {
                    entry.method().invoke(entry.instance(), concreteEvent);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                if (finalHookManager != null)
                    finalHookManager.callAndClearHooks(
                            entry.instance().getClass(), false);
            });
        } else {
            entry.method().invoke(entry.instance(), concreteEvent);

            if (hookManager != null)
                hookManager.callAndClearHooks(
                        entry.instance().getClass(), false);
        }
    }

    public static void callCustomEvent(Event event) {
        if (event instanceof PlayerEvent playerEvent) {
            if (HypixelDataHandler.getUser(playerEvent.getPlayer()) == null) {
                Logger.warn("Tried to call custom event {} for player {} but their data is not loaded.",
                        event.getClass().getSimpleName(),
                        playerEvent.getPlayer().getUsername());
                return;
            }
        }
        rootNode.call(event);
    }

    private static EventMethodEntry eventMethodEntry(Object instance, Method method) {
        if (method.isAnnotationPresent(PhasedEvent.class)) {
            PhasedEvent phasedEvent = method.getAnnotation(PhasedEvent.class);
            return new EventMethodEntry(
                    method,
                    instance,
                    phasedEvent.node(),
                    phasedEvent.phase(),
                    phasedEvent.order(),
                    phasedEvent.requireDataLoaded(),
                    phasedEvent.isAsync());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static void registerEntry(EventMethodEntry entry) {
        try {
            Class<? extends Event> eventType = (Class<? extends Event>) entry.method().getParameterTypes()[0];
            EventNode<Event> listenerNode = (EventNode<Event>) entry.node().newNode(
                    entry.phase().name().toLowerCase() + "-" + entry.instance().getClass().getName().replace('.', '-') + "-" + entry.method().getName(),
                    entry.order());

            listenerNode.addListener(eventType, rawEvent -> {
                Event concreteEvent = eventType.cast(rawEvent);
                runOrWaitForData(entry, concreteEvent);
            });

            entry.phase().node().addChild(listenerNode);
        } catch (Exception e) {
            Logger.error(e, "Error occurred while registering event: {}", entry.instance().getClass().getSimpleName());
        }
    }

    private static void runOrWaitForData(EventMethodEntry entry, Event concreteEvent) {
        if (concreteEvent instanceof PlayerEvent event
                && entry.requireDataLoaded()
                && (HypixelDataHandler.getUser(event.getPlayer()) == null
                || (HypixelConst.isIslandServer() && !((HypixelPlayer) event.getPlayer()).isReadyForEvents()))) {
            Scheduler scheduler = MinecraftServer.getSchedulerManager();

            scheduler.submitTask(() -> {
                Player player = event.getPlayer();
                if (!player.isOnline()) return TaskSchedule.stop();
                if (HypixelDataHandler.getUser(player) == null) return TaskSchedule.millis(2);
                if (HypixelConst.isIslandServer() && !((HypixelPlayer) player).isReadyForEvents()) return TaskSchedule.millis(2);

                runEvent(entry, concreteEvent);
                return TaskSchedule.stop();
            });
            return;
        }

        try {
            runEvent(entry, concreteEvent);
        } catch (Exception ex) {
            Logger.error(ex, "Exception occurred while running event {} with event type {}",
                    entry.method().getClass().getSimpleName(),
                    concreteEvent.getClass().getSimpleName());
        }
    }
}
