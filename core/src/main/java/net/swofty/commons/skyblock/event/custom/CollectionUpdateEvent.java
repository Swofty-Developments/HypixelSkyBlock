package net.swofty.commons.skyblock.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

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
