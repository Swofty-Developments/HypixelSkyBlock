package net.swofty.type.skyblockgeneric.calendar;

import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.protocol.objects.darkauction.TriggerDarkAuctionProtocol;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public record CalendarEvent(
    String id,
    ItemStack representation,
    Function<Integer, String> displayName,
    List<String> description,
    List<Long> times,
    Duration duration,
    boolean tracksYear,
    BiConsumer<Long, Integer> action
) {
    private static final long THREE_DAYS = 3 * 20 * 60 * 24; // 3 SkyBlock days in ticks
    private static final long YEAR = 20 * 60 * 24 * 31 * 12; // Full SkyBlock year in ticks
    private static final long MONTH = 744000L;
    private static final Map<Long, List<CalendarEvent>> eventCache = new HashMap<>();
    private static final List<CalendarEvent> allEvents = new ArrayList<>();

    public static CalendarEvent NEW_YEAR = new CalendarEvent(
        "new_year",
        ItemStack.of(Material.CAKE).with(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true),
        year -> "§d" + StringUtility.ntify(year) + " New Year Celebration",
        List.of(
            "§7To celebrate the SkyBlock New Year,",
            "§7the Baker is giving out free Cake!"
        ),
        List.of(10L),
        Duration.ofHours(1),
        true,
        (time, year) -> {
            // New Year's actions
        }
    );

    // Dark Auction occurs every 3 SkyBlock days at midnight
    public static CalendarEvent DARK_AUCTION = new CalendarEvent(
        "dark_auction",
        ItemStack.of(Material.NETHER_STAR).with(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true),
        year -> "§5Dark Auction",
        List.of(
            "§7The Dark Auction is a secret",
            "§7underground auction where",
            "§7special items are sold."
        ),
        calculateDarkAuctionTimes(),
        Duration.ofMinutes(5),
        false,
        (time, year) -> {
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
        }
    );

    private static List<Long> calculateDarkAuctionTimes() {
        List<Long> times = new ArrayList<>();
        // Every 3 days at midnight (0, 72000, 144000, ...)
        for (long time = 0; time < YEAR; time += THREE_DAYS) {
            times.add(time);
        }
        return times;
    }

    public static CalendarEvent ELECTION_OPEN = new CalendarEvent(
        "election_open",
        ItemStack.of(Material.JUKEBOX),
        year -> "§b" + StringUtility.ntify(year) + " Election Booth Opens",
        List.of(
            "§7The Mayor Election voting booth",
            "§7is now open! Cast your vote for",
            "§7your favorite candidate."
        ),
        List.of(5 * MONTH),
        Duration.ofHours(0),
        true,
        (_, _) -> {
            ElectionManager.onElectionStart();
        }
    );

    public static CalendarEvent ELECTION_CLOSE = new CalendarEvent(
        "election_close",
        ItemStack.of(Material.JUKEBOX),
        year -> "§b" + StringUtility.ntify(year) + " Election Over!",
        List.of(
            "§7The Mayor Election has concluded!",
            "§7A new Mayor has been elected."
        ),
        List.of(8 * MONTH),
        Duration.ofHours(0),
        true,
        (_, _) -> {
            ElectionManager.onElectionEnd();
        }
    );

    static {
        registerEvent(NEW_YEAR);
        registerEvent(DARK_AUCTION);
        registerEvent(ELECTION_OPEN);
        registerEvent(ELECTION_CLOSE);
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
        List<CalendarEvent> activeEvents = new ArrayList<>();
        for (CalendarEvent event : allEvents) {
            for (Long eventStartTime : event.times()) {
                // convert ticks to milliseconds
                Duration timeSinceEventStart = Duration.ofMillis((time - eventStartTime) * 50);

                // checks if the event is currently active
                if (!timeSinceEventStart.isNegative() && timeSinceEventStart.compareTo(event.duration()) < 0) {
                    activeEvents.add(event);
                    break;
                }
            }
        }
        return activeEvents;
    }

    public static List<CalendarEvent> getAllEvents() {
        return new ArrayList<>(allEvents);
    }

    public static Map<Long, List<CalendarEvent>> getEventCache() {
        return new HashMap<>(eventCache);
    }

    public static CalendarEvent fromId(String id) {
        for (CalendarEvent event : allEvents) {
            if (event.id().equalsIgnoreCase(id)) {
                return event;
            }
        }
        return null;
    }
}