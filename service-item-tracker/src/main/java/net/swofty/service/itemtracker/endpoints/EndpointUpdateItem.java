package net.swofty.service.itemtracker.endpoints;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.itemtracker.TrackedItemsDatabase;

import java.util.*;

public class EndpointUpdateItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "update-tracked-item";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        UUID itemUUID = UUID.fromString((String) messageData.get("item-uuid"));
        UUID attachedPlayerUUID = UUID.fromString((String) messageData.get("attached-player-uuid"));
        UUID attachedPlayerProfileUUID = UUID.fromString((String) messageData.get("attached-player-profile"));
        String attachedItemType = (String) messageData.get("item-type");

        Thread.startVirtualThread(() -> {
            if (!new TrackedItemsDatabase(itemUUID).exists()) {
                TrackedItem item = TrackedItem.newTrackedItem(
                        itemUUID,
                        attachedPlayerUUID,
                        attachedPlayerProfileUUID,
                        attachedItemType,
                        TrackedItemsDatabase.getNumberMade(attachedItemType)
                );
                new TrackedItemsDatabase(itemUUID).insertOrUpdate(item);
            } else {
                TrackedItem item = new TrackedItemsDatabase(itemUUID).get();
                item.addOrUpdateAttachedPlayer(attachedPlayerUUID, attachedPlayerProfileUUID);
                new TrackedItemsDatabase(itemUUID).insertOrUpdate(item);
            }
        });

        return new HashMap<>();
    }
}
