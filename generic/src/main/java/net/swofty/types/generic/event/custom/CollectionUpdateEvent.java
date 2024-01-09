package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;

@Getter
public class CollectionUpdateEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final ItemType itemType;
    private final int oldValue;

    public CollectionUpdateEvent(SkyBlockPlayer player, ItemType itemType, int oldValue) {
        this.player = player;
        this.itemType = itemType;
        this.oldValue = oldValue;
    }
}
