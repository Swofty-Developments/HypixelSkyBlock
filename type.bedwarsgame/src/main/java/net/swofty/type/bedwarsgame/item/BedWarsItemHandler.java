package net.swofty.type.bedwarsgame.item;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.type.bedwarsgame.item.impl.Fireball;
import net.swofty.type.bedwarsgame.item.impl.GoldenApple;

import java.util.ArrayList;
import java.util.List;

public class BedWarsItemHandler {
	private final List<BedWarsItem> items = new ArrayList<>();

	public BedWarsItemHandler() {
		add(new Fireball());
		add(new GoldenApple());
	}

	private void add(BedWarsItem item) {
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
			}
		}
	}

	public void onItemUse(PlayerUseItemEvent event) {
		for (BedWarsItem item : items) {
			ItemStack itemStack = event.getItemStack();
			if (isItem(item, itemStack)) {
				item.onItemUse(event);
			}
		}
	}

	private boolean isItem(BedWarsItem item, ItemStack itemStack) {
		String itemId = itemStack.get(DataComponents.CUSTOM_DATA).getTag(Tag.String("item"));
		return itemId != null && itemId.equalsIgnoreCase(item.getId());
	}

}