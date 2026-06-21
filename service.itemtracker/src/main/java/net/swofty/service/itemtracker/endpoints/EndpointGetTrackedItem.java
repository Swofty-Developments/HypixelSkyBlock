package net.swofty.service.itemtracker.endpoints;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.itemtracker.TrackedItemsDatabase;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointGetTrackedItem implements RedisMessageHandler<
        TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage,
        TrackedItemRetrieveProtocol.TrackedItemResponse> {

    @Override
    public TrackedItemRetrieveProtocol protocol() {
        return new TrackedItemRetrieveProtocol();
    }

    @Override
    public TrackedItemRetrieveProtocol.TrackedItemResponse handle(TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage messageObject, RedisMessageContext context) {
        UUID itemUUID = messageObject.itemUUID();

        if (!new TrackedItemsDatabase(itemUUID).exists()) {
            throw new RuntimeException("Attempted to get a tracked item that does not exist.");
        }

        TrackedItem item = new TrackedItemsDatabase(itemUUID).get();
        return new TrackedItemRetrieveProtocol.TrackedItemResponse(item, true, null);
    }
}