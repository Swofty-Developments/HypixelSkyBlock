package net.swofty.service.itemtracker.endpoints;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.itemtracker.TrackedItemsDatabase;

import java.util.UUID;

public class EndpointGetTrackedItem implements ServiceEndpoint<
        TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage,
        TrackedItemRetrieveProtocolObject.TrackedItemResponse> {

    @Override
    public TrackedItemRetrieveProtocolObject associatedProtocolObject() {
        return new TrackedItemRetrieveProtocolObject();
    }

    @Override
    public TrackedItemRetrieveProtocolObject.TrackedItemResponse onMessage(ServiceProxyRequest message, TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage messageObject) {
        UUID itemUUID = messageObject.itemUUID();

        if (!new TrackedItemsDatabase(itemUUID).exists()) {
            throw new RuntimeException("Attempted to get a tracked item that does not exist.");
        }

        TrackedItem item = new TrackedItemsDatabase(itemUUID).get();
        return new TrackedItemRetrieveProtocolObject.TrackedItemResponse(item);
    }
}