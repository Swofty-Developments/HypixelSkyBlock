package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.proxyapi.impl.ProxyUnderstandableEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class CollectionUpdateEvent implements PlayerInstanceEvent, ProxyUnderstandableEvent {
    private final SkyBlockPlayer player;
    private final ItemTypeLinker itemTypeLinker;
    private final int oldValue;

    public CollectionUpdateEvent(SkyBlockPlayer player, ItemTypeLinker itemTypeLinker, int oldValue) {
        this.player = player;
        this.itemTypeLinker = itemTypeLinker;
        this.oldValue = oldValue;
    }

    @Override
    public String asProxyUnderstandable() {
        return itemTypeLinker.toString() + "," + oldValue;
    }

    public static CollectionUpdateEvent fromProxyUnderstandable(SkyBlockPlayer player, String string) {
        String[] split = string.split(",");
        ItemTypeLinker type = ItemTypeLinker.valueOf(split[0]);
        int oldValue = Integer.parseInt(split[1]);
        return new CollectionUpdateEvent(player, type, oldValue);
    }
}
