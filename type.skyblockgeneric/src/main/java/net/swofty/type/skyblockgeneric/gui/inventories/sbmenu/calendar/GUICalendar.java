package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.calendar;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GUICalendar extends StatelessView {

    private static final int[] EVENT_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Calendar and Events", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 40);
        Components.back(layout, 39, ctx);

        boolean includeDarkAuction = ctx.player().getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_VISITED_DARK_AUCTION);

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> initialEvents = includeDarkAuction
            ? SkyBlockCalendar.getEventsWithDurationUntil(14)
            : SkyBlockCalendar.getEventsWithDurationUntilSkipSpecific(14, Collections.singletonList(CalendarEvent.DARK_AUCTION));
        List<CalendarEvent> orderedEvents = new ArrayList<>(initialEvents.values());

        for (int i = 0; i < Math.min(orderedEvents.size(), EVENT_SLOTS.length); i++) {
            final CalendarEvent event = orderedEvents.get(i);
            final int slot = EVENT_SLOTS[i];

            layout.autoUpdating(slot, (_, _) -> {
                Map<SkyBlockCalendar.EventInfo, CalendarEvent> freshEvents = includeDarkAuction
                    ? SkyBlockCalendar.getEventsWithDurationUntil(14)
                    : SkyBlockCalendar.getEventsWithDurationUntilSkipSpecific(14, Collections.singletonList(CalendarEvent.DARK_AUCTION));

                SkyBlockCalendar.EventInfo freshInfo = freshEvents.entrySet().stream()
                    .filter(e -> e.getValue() == event)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

                if (freshInfo == null) {
                    return event.representation().builder()
                        .set(DataComponents.CUSTOM_NAME, Component.text(event.getDisplayName(0)))
                        .set(DataComponents.LORE, List.of());
                }

                List<Component> loreHeader = new ArrayList<>(List.of(
                    Component.text("§7Starts in: §e" + StringUtility.formatTimeLeft(freshInfo.timeUntilBegin() * 50L))
                ));
                if (!freshInfo.duration().isZero()) {
                    loreHeader.add(Component.text("§7Event lasts for §e" + StringUtility.formatTimeLeft(freshInfo.duration().toMillis()) + "§7!"));
                }
                loreHeader.add(Component.text(" "));

                List<Component> lore = Stream.concat(
                    loreHeader.stream(),
                    event.description().stream().map(line -> (Component) Component.text(line))
                ).toList();

                return event.representation().builder()
                    .set(DataComponents.CUSTOM_NAME, Component.text(event.getDisplayName(freshInfo.year())))
                    .set(DataComponents.LORE, lore);
            }, Duration.ofSeconds(1));
        }
    }
}