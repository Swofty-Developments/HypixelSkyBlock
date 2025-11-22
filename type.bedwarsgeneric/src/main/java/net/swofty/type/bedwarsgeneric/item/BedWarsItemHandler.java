package net.swofty.type.bedwarsgeneric.item;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class BedWarsItemHandler {
	private final List<BedWarsItem> items = new ArrayList<>();

	public void add(BedWarsItem item) {
		items.add(item);
	}

	public BedWarsItem getItem(String name) {
		return items.stream().filter(
				p -> p.getId().equalsIgnoreCase(name)
		).findFirst().orElseThrow();
	}

	public void onItemFinishUse(PlayerFinishItemUseEvent event) {
		for (BedWarsItem item : items) {
			ItemStack itemStack = event.getItemStack();
			if (isItem(item, itemStack)) {
				item.onItemFinishUse(event);
			}
		}
	}

	public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {
		for (BedWarsItem item : items) {
			ItemStack itemStack = event.getItemStack();
			if (isItem(item, itemStack)) {
				item.onItemUseOnBlock(event);
				item.onItemInteract(event);
			}
		}
	}

	public void onItemUse(PlayerUseItemEvent event) {
		for (BedWarsItem item : items) {
			ItemStack itemStack = event.getItemStack();
			if (isItem(item, itemStack)) {
				item.onItemUse(event);
				item.onItemInteract(event);
			}
		}
	}

	public void onItemDrop(ItemDropEvent event) {
		for (BedWarsItem item : items) {
			ItemStack itemStack = event.getItemStack();
			if (isItem(item, itemStack)) {
				item.onItemDrop(event);
			}
		}
	}

	public void onBlockPlace(PlayerBlockPlaceEvent event) {
		for (BedWarsItem item : items) {
			ItemStack itemStack = event.getPlayer().getItemInMainHand();
			if (isItem(item, itemStack)) {
				item.onBlockPlace(event);
			}
		}
	}

	private boolean isItem(BedWarsItem item, ItemStack itemStack) {
		CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
		if (data == null) return false;
		String id = data.getTag(Tag.String("item"));
		return id != null && id.equalsIgnoreCase(item.getId());
	}

}