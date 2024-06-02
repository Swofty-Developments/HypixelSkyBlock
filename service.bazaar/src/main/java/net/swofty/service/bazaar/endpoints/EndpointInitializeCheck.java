package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.bazaar.BazaarDatabase;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class EndpointInitializeCheck implements ServiceEndpoint {
    @Override
    public String channel() {
        return "bazaar-initialize";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        BazaarInitializationRequest request = (BazaarInitializationRequest) messageData.get("init-request");

        if (!BazaarService.getCacheService().isEmpty()) return new HashMap<>();

        request.itemsToInitialize().stream().parallel().forEach(entry -> {
            if (BazaarDatabase.collection.find(new Document("_id", entry)).first() == null) {
                BazaarItem item = new BazaarItem(entry,
                        new HashMap<>(), new HashMap<>());

                BazaarDatabase.collection.insertOne(item.toDocument());
            }
        });


        return new HashMap<>();
    }
}
