package net.swofty.service.itemtracker.endpoints;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemUpdateProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.itemtracker.TrackedItemsDatabase;

import java.util.*;

public class EndpointUpdateItem implements ServiceEndpoint<
        TrackedItemUpdateProtocolObject.TrackedItemUpdateMessage,
        TrackedItemUpdateProtocolObject.TrackedItemUpdateResponse> {

    @Override
    public TrackedItemUpdateProtocolObject associatedProtocolObject() {
        return new TrackedItemUpdateProtocolObject();
    }

    @Override
    public TrackedItemUpdateProtocolObject.TrackedItemUpdateResponse onMessage(ServiceProxyRequest message, TrackedItemUpdateProtocolObject.TrackedItemUpdateMessage messageObject) {
        UUID itemUUID = messageObject.itemUUID();
        UUID attachedPlayerUUID = messageObject.attachedPlayerUUID();
        UUID attachedPlayerProfile = messageObject.attachedPlayerProfile();
        String itemType = messageObject.itemType();

        Thread.startVirtualThread(() -> {
            if (!new TrackedItemsDatabase(itemUUID).exists()) {
                TrackedItem item = TrackedItem.newTrackedItem(
                        itemUUID,
                        attachedPlayerUUID,
                        attachedPlayerProfile,
                        itemType,
                        TrackedItemsDatabase.getNumberMade(itemType)
                );
                new TrackedItemsDatabase(itemUUID).insertOrUpdate(item);
            } else {
                TrackedItem item = new TrackedItemsDatabase(itemUUID).get();
                item.addOrUpdateAttachedPlayer(attachedPlayerUUID, attachedPlayerProfile);
                new TrackedItemsDatabase(itemUUID).insertOrUpdate(item);
            }
        });

        return new TrackedItemUpdateProtocolObject.TrackedItemUpdateResponse();
    }
}
