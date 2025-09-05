package net.swofty.type.bedwarsgeneric.item;

import lombok.Getter;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.swofty.type.generic.user.HypixelPlayer;

/**
 * Represents a custom item in a BedWars game. Handles in-game events and properties of items.
 */
@Getter
public abstract class BedWarsItem {

	private final String id;

	public BedWarsItem(String id) {
		this.id = id;
	}

	public abstract ItemStack getBlandItem();

	public ItemStack getItemStack(HypixelPlayer... player) {
		return getBlandItem().with(DataComponents.CUSTOM_DATA, new CustomData(CompoundBinaryTag.builder().putString("item", id).build()));
	}

	public void onItemFinishUse(PlayerFinishItemUseEvent event) {
		// stub
	}

	public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {
		// stub
	}

	public void onItemUse(PlayerUseItemEvent event) {
		// stub
	}

	public void onItemDrop(ItemDropEvent event) {
		// stub
	}

	public void onItemInteract(PlayerInstanceEvent event) {
		// stub
	}

	public void onBlockPlace(PlayerBlockPlaceEvent event) {
		// stub
	}

}
