package net.swofty.type.murdermysterygame.item;

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

@Getter
public abstract class SimpleInteractableItem {

    private final String id;

    public SimpleInteractableItem(String id) {
        this.id = id;
    }

    public abstract ItemStack getBlandItem();

    public ItemStack getItemStack() {
        return getBlandItem().with(DataComponents.CUSTOM_DATA,
                new CustomData(CompoundBinaryTag.builder().putString("item", id).build()));
    }

    public void onItemFinishUse(PlayerFinishItemUseEvent event) {}

    public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {}

    public void onItemUse(PlayerUseItemEvent event) {}

    public void onItemDrop(ItemDropEvent event) {}

    public void onItemInteract(PlayerInstanceEvent event) {}

    public void onBlockPlace(PlayerBlockPlaceEvent event) {}
}
