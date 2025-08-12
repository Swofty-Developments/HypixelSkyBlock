package net.swofty.type.skyblockgeneric.item.components;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.item.handlers.place.PlaceEventHandler;
import net.swofty.type.generic.item.handlers.place.PlaceEventRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

public class PlaceEventComponent extends SkyBlockItemComponent {
    private final String handlerId;

    public PlaceEventComponent(String handlerId) {
        this.handlerId = handlerId;
    }

    public void handlePlace(PlayerBlockPlaceEvent event, HypixelPlayer player, SkyBlockItem item) {
        PlaceEventHandler handler = PlaceEventRegistry.getHandler(handlerId);
        if (handler != null) {
            handler.onPlace(event, player, item);
        }
    }
}
