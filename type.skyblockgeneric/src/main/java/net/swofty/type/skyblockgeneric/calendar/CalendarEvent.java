package net.swofty.type.skyblockgeneric.calendar;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.darkauction.TriggerDarkAuctionProtocol;
import net.swofty.proxyapi.ProxyService;
import org.tinylog.Logger;

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

    // Dark Auction occurs every 3 SkyBlock days at midnight
    public static CalendarEvent DARK_AUCTION = new CalendarEvent(calculateDarkAuctionTimes(), time -> {
        Logger.info("Dark Auction calendar event triggered at time: {}", time);
        ProxyService darkAuctionService = new ProxyService(ServiceType.DARK_AUCTION);
        darkAuctionService.handleRequest(new TriggerDarkAuctionProtocol.TriggerMessage(time))
                .thenAccept(response -> {
                    if (response instanceof TriggerDarkAuctionProtocol.TriggerResponse triggerResponse) {
                        if (triggerResponse.success()) {
                            Logger.info("Dark Auction started successfully");
                        } else {
                            Logger.debug("Dark Auction trigger: {}", triggerResponse.message());
                        }
                    }
                })
                .exceptionally(throwable -> {
                    Logger.error(throwable, "Failed to trigger Dark Auction");
                    return null;
                });
    });

    private static List<Long> calculateDarkAuctionTimes() {
        List<Long> times = new ArrayList<>();
        // Every 3 days at midnight (0, 72000, 144000, ...)
        for (long time = 0; time < YEAR; time += THREE_DAYS) {
            times.add(time);
        }
        return times;
    }

    static {
        registerEvent(NEW_YEAR);
        registerEvent(DARK_AUCTION);
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