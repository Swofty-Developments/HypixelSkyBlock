package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarInitializationRequest;
import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.bazaar.BazaarDatabase;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;
import org.json.JSONObject;

public class EndpointInitializeCheck implements ServiceEndpoint {
    @Override
    public String channel() {
        return "bazaar-initialize";
    }

    @Override
    public JSONObject onMessage(ServiceProxyRequest message) {
        if (!BazaarService.getCacheService().isEmpty()) return new JSONObject();

        BazaarInitializationRequest request = BazaarInitializationRequest.deserialize(
                new JSONObject(message.getMessage()).getString("init-request")
        );

        request.itemsToInitialize().entrySet().stream().parallel().forEach(entry -> {
            if (BazaarDatabase.collection.find(new Document("_id", entry.getKey())).first() == null) {
                BazaarItem item = new BazaarItem(entry.getKey());
                item.setBuyPrice(entry.getValue().getKey());
                item.setSellPrice(entry.getValue().getValue());

                BazaarDatabase.collection.insertOne(item.toDocument());
            }
        });


        return new JSONObject();
    }
}
