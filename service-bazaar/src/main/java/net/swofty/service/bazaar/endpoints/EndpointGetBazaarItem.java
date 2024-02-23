package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;
import org.json.JSONObject;

public class EndpointGetBazaarItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "bazaar-get-item";
    }

    @Override
    public JSONObject onMessage(ServiceProxyRequest message) {
        String itemName = new JSONObject(message.getMessage()).getString("item-name");

        Document document = BazaarService.getCacheService().getItem(itemName);

        return new JSONObject().put("item", document.toJson());
    }
}
