package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.calendar;

import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GUICalendar extends HypixelInventoryGUI {

	public GUICalendar() {
		super("Calendar and Events", InventoryType.CHEST_5_ROW);
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(FILLER_ITEM);

		Map<SkyBlockCalendar.EventInfo, CalendarEvent> events = SkyBlockCalendar.getEventsWithDurationUntil(14);

		int[] eventSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 26};
		int index = 0;
		for (Map.Entry<SkyBlockCalendar.EventInfo, CalendarEvent> entry : events.entrySet()) {
			if (index >= eventSlots.length) break;
			SkyBlockCalendar.EventInfo info = entry.getKey();
			CalendarEvent event = entry.getValue();
			set(new GUIItem(eventSlots[index]) {
					@Override
					public ItemStack.Builder getItem(HypixelPlayer player) {
						List<Component> loreHeader = List.of(
								Component.text("§7Starts in: §e" + StringUtility.formatTimeLeft(info.timeUntilBegin())),
								Component.text("§7Event lasts for §e" +  StringUtility.formatTimeLeft(info.duration()) + "§7!"),
								Component.text(" ")
						);
						List<Component> loreFooter = event.description().stream()
								.map(line -> (Component) Component.text(line))
								.toList();

						List<Component> lore = Stream.concat(loreHeader.stream(), loreFooter.stream())
								.toList();

						return event.representation().builder()
								.set(DataComponents.CUSTOM_NAME, Component.text(event.getDisplayName(info.year())))
								.set(DataComponents.LORE, lore);
					}
				});
			index++;
		}


		set(GUIClickableItem.getGoBackItem(39, new GUISkyBlockMenu()));
		set(GUIClickableItem.getCloseItem(40));
		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}
}
