package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarInitializeProtocolObject;
import net.swofty.service.bazaar.BazaarDatabase;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.HashMap;

public class EndpointInitializeCheck implements ServiceEndpoint<
        BazaarInitializeProtocolObject.BazaarInitializationRequest,
        BazaarInitializeProtocolObject.BazaarInitializeResponse> {

    @Override
    public ProtocolObject<BazaarInitializeProtocolObject.BazaarInitializationRequest, BazaarInitializeProtocolObject.BazaarInitializeResponse> associatedProtocolObject() {
        return new BazaarInitializeProtocolObject();
    }

    @Override
    public BazaarInitializeProtocolObject.BazaarInitializeResponse onMessage(ServiceProxyRequest message, BazaarInitializeProtocolObject.BazaarInitializationRequest messageObject) {
        if (!BazaarService.getCacheService().isEmpty()) return new BazaarInitializeProtocolObject.BazaarInitializeResponse();

        messageObject.itemsToInitialize().stream().parallel().forEach(entry -> {
            if (BazaarDatabase.collection.find(new Document("_id", entry)).first() == null) {
                BazaarItem item = new BazaarItem(entry,
                        new HashMap<>(), new HashMap<>());

                BazaarDatabase.collection.insertOne(item.toDocument());
            }
        });

        return new BazaarInitializeProtocolObject.BazaarInitializeResponse();
    }
}
