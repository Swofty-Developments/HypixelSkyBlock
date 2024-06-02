package net.swofty.types.generic.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record CalendarEvent(List<Long> times, Consumer<Long> action) {
    private static final Map<Long, List<CalendarEvent>> eventCache = new HashMap<>();

    public static CalendarEvent NEW_YEAR = new CalendarEvent(List.of(10L), time -> {
        // New Year's actions
    });

    static {
        registerEvent(NEW_YEAR);
    }

    private static void registerEvent(CalendarEvent event) {
        for (Long time : event.times()) {
            eventCache.computeIfAbsent(time, k -> new ArrayList<>()).add(event);
        }
    }

    public static List<CalendarEvent> getCurrentEvents(long time) {
        return eventCache.getOrDefault(time, new ArrayList<>());
    }
}