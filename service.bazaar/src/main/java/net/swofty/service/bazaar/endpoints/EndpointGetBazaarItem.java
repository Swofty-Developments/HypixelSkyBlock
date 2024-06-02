package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class EndpointGetBazaarItem implements ServiceEndpoint {
    @Override
    public String channel() {
        return "bazaar-get-item";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        String itemName = (String) messageData.get("item-name");

        Document document = BazaarService.getCacheService().getItem(itemName);

        HashMap<String, Object> response = new HashMap<>();
        response.put("item", BazaarItem.fromDocument(document));
        return response;
    }
}
