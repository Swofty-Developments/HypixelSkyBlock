package net.swofty.type.skyblockgeneric.calendar;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record CalendarEvent(
        ItemStack representation,
        Function<Integer, String> displayName,
        List<String> description,
        List<Long> times,
        long duration,
        boolean tracksYear,
        BiConsumer<Long, Integer> action
) {
    private static final Map<Long, List<CalendarEvent>> eventCache = new HashMap<>();
    private static final List<CalendarEvent> allEvents = new ArrayList<>();

    public static CalendarEvent NEW_YEAR = new CalendarEvent(
            ItemStack.of(Material.CAKE).with(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true),
            year -> "§d" + StringUtility.ntify(year) + " New Year Celebration",
            List.of(
                    "§7To celebrate the SkyBlock New Year,",
                    "§7the Baker is giving out free Cake!"
            ),
            List.of(10L),
            20 * 60 * 60L, // 1 hour
            true,
            (time, year) -> {
                // New Year's actions
            }
    );

    static {
        registerEvent(NEW_YEAR);
    }

    public String getDisplayName(int year) {
        return displayName.apply(year);
    }

    private static void registerEvent(CalendarEvent event) {
        allEvents.add(event);
        for (Long time : event.times()) {
            eventCache.computeIfAbsent(time, k -> new ArrayList<>()).add(event);
        }
    }

    public static List<CalendarEvent> getCurrentEvents(long time) {
        return eventCache.getOrDefault(time, new ArrayList<>());
    }

    public static List<CalendarEvent> getAllEvents() {
        return new ArrayList<>(allEvents);
    }

    public static Map<Long, List<CalendarEvent>> getEventCache() {
        return new HashMap<>(eventCache);
    }
}