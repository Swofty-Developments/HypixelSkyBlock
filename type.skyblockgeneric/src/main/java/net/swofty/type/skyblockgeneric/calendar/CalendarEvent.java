package net.swofty.type.skyblockgeneric.calendar;

import net.swofty.commons.ServiceType;
import net.swofty.commons.protocol.objects.darkauction.TriggerDarkAuctionProtocol;
import net.swofty.proxyapi.ProxyService;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record CalendarEvent(List<Long> times, Consumer<Long> action) {
    private static final Map<Long, List<CalendarEvent>> eventCache = new HashMap<>();

    // Time constants
    private static final int YEAR = 8928000;
    private static final int DAY = 24000;
    private static final int THREE_DAYS = DAY * 3; // 72000 ticks

    public static CalendarEvent NEW_YEAR = new CalendarEvent(List.of(10L), time -> {
        // New Year's actions
    });

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

    private static void registerEvent(CalendarEvent event) {
        for (Long time : event.times()) {
            eventCache.computeIfAbsent(time, k -> new ArrayList<>()).add(event);
        }
    }

    public static List<CalendarEvent> getCurrentEvents(long time) {
        return eventCache.getOrDefault(time, new ArrayList<>());
    }
}