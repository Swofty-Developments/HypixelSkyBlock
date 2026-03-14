package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.calendar;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;

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

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> events = SkyBlockCalendar.getEventsWithDurationUntil(14);

        int index = 0;
        for (Map.Entry<SkyBlockCalendar.EventInfo, CalendarEvent> entry : events.entrySet()) {
            if (index >= EVENT_SLOTS.length) break;
            SkyBlockCalendar.EventInfo info = entry.getKey();
            CalendarEvent event = entry.getValue();
            int slot = EVENT_SLOTS[index];

            layout.slot(slot, (s, c) -> {
                List<Component> loreHeader = List.of(
                        Component.text("§7Starts in: §e" + StringUtility.formatTimeLeft(info.timeUntilBegin() * 50L)),
                        Component.text("§7Event lasts for §e" + StringUtility.formatTimeLeft(info.duration().toMillis()) + "§7!"),
                        Component.text(" ")
                );
                List<Component> loreFooter = event.description().stream()
                        .map(line -> (Component) Component.text(line))
                        .toList();

                List<Component> lore = Stream.concat(loreHeader.stream(), loreFooter.stream()).toList();

                return event.representation().builder()
                        .set(DataComponents.CUSTOM_NAME, Component.text(event.getDisplayName(info.year())))
                        .set(DataComponents.LORE, lore);
            });
            index++;
        }
    }
}
