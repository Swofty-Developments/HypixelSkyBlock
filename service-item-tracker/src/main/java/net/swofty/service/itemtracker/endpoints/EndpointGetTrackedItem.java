package net.swofty.service.itemtracker.endpoints;

import net.swofty.commons.TrackedItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.itemtracker.TrackedItemsDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EndpointGetTrackedItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "get-tracked-item";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        UUID itemUUID = UUID.fromString((String) messageData.get("item-uuid"));

        if (!new TrackedItemsDatabase(itemUUID).exists()) {
            throw new RuntimeException("Attempted to get a tracked item that does not exist.");
        }

        TrackedItem item = new TrackedItemsDatabase(itemUUID).get();
        return new HashMap<>() {{
            put("tracked-item", item);
        }};
    }
}