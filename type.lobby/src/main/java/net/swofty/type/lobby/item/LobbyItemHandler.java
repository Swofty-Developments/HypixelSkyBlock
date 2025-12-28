package net.swofty.type.lobby.item;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.tag.Tag;
import net.swofty.type.lobby.item.impl.CancelParkour;
import net.swofty.type.lobby.item.impl.LastCheckpoint;
import net.swofty.type.lobby.item.impl.ResetParkour;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LobbyItemHandler {
    private final List<LobbyItem> items = new ArrayList<>(
            List.of(
                    new LastCheckpoint(),
                    new CancelParkour(),
                    new ResetParkour()
            )
    );

    public void add(LobbyItem item) {
        items.add(item);
    }

    public Optional<LobbyItem> getItem(String name) {
        return items.stream()
                .filter(p -> p.getId().equalsIgnoreCase(name))
                .findFirst();
    }

    public void onItemFinishUse(PlayerFinishItemUseEvent event) {
        for (LobbyItem item : items) {
            ItemStack itemStack = event.getItemStack();
            if (isItem(item, itemStack)) {
                item.onItemFinishUse(event);
            }
        }
    }

    public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {
        for (LobbyItem item : items) {
            ItemStack itemStack = event.getItemStack();
            if (isItem(item, itemStack)) {
                item.onItemUseOnBlock(event);
                item.onItemInteract(event);
            }
        }
    }

    public void onItemUse(PlayerUseItemEvent event) {
        for (LobbyItem item : items) {
            ItemStack itemStack = event.getItemStack();
            if (isItem(item, itemStack)) {
                item.onItemUse(event);
                item.onItemInteract(event);
            }
        }
    }

    public void onItemDrop(ItemDropEvent event) {
        for (LobbyItem item : items) {
            ItemStack itemStack = event.getItemStack();
            if (isItem(item, itemStack)) {
                item.onItemDrop(event);
            }
        }
    }

    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        for (LobbyItem item : items) {
            ItemStack itemStack = event.getPlayer().getItemInMainHand();
            if (isItem(item, itemStack)) {
                item.onBlockPlace(event);
                item.onItemInteract(event);
            }
        }
    }

    private boolean isItem(LobbyItem item, ItemStack itemStack) {
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;
        String id = data.getTag(Tag.String("item"));
        return id != null && id.equalsIgnoreCase(item.getId());
    }
}
