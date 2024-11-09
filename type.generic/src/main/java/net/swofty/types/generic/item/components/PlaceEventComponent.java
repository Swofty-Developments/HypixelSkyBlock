package net.swofty.types.generic.item.components;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.place.PlaceEventHandler;
import net.swofty.types.generic.item.handlers.place.PlaceEventRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class PlaceEventComponent extends SkyBlockItemComponent {
    private final String handlerId;

    public PlaceEventComponent(String handlerId) {
        this.handlerId = handlerId;
    }

    public void handlePlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        PlaceEventHandler handler = PlaceEventRegistry.getHandler(handlerId);
        if (handler != null) {
            handler.onPlace(event, player, item);
        }
    }
}
