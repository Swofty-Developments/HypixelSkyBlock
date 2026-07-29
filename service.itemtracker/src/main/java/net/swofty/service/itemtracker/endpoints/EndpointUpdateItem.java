package net.swofty.service.itemtracker.endpoints;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemUpdateProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.service.itemtracker.TrackedItemsDatabase;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointUpdateItem implements RedisMessageHandler<
        TrackedItemUpdateProtocol.TrackedItemUpdateMessage,
        TrackedItemUpdateProtocol.TrackedItemUpdateResponse> {

    @Override
    public TrackedItemUpdateProtocol protocol() {
        return new TrackedItemUpdateProtocol();
    }

    @Override
    public TrackedItemUpdateProtocol.TrackedItemUpdateResponse handle(TrackedItemUpdateProtocol.TrackedItemUpdateMessage messageObject, RedisMessageContext context) {
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

        return new TrackedItemUpdateProtocol.TrackedItemUpdateResponse();
    }
}
