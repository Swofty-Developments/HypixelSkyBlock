package net.swofty.type.bedwarsgame.item;

import lombok.Getter;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;

/**
 * WIP: Represents a custom item in a BedWars game. Handles in-game events and properties of items.
 */
@Getter
public abstract class BedWarsItem {

	private final String id;

	public BedWarsItem(String id) {
		this.id = id;
	}

	public abstract ItemStack getBlandItem();

    public ItemStack getItemstack() {
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

}
