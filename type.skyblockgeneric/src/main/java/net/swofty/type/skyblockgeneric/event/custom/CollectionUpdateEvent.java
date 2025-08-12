package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.commons.item.ItemType;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.type.generic.user.HypixelPlayer;

@Getter
public class CollectionUpdateEvent implements PlayerInstanceEvent, ProxyUnderstandableEvent {
    private final HypixelPlayer player;
    private final ItemType itemType;
    private final int oldValue;

    public CollectionUpdateEvent(HypixelPlayer player, ItemType itemType, int oldValue) {
        this.player = player;
        this.itemType = itemType;
        this.oldValue = oldValue;
    }

    @Override
    public String asProxyUnderstandable() {
        return itemType.toString() + "," + oldValue;
    }

    public static CollectionUpdateEvent fromProxyUnderstandable(HypixelPlayer player, String string) {
        String[] split = string.split(",");
        ItemType type = ItemType.valueOf(split[0]);
        int oldValue = Integer.parseInt(split[1]);
        return new CollectionUpdateEvent(player, type, oldValue);
    }
}
