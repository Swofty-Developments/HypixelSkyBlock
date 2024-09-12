package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocolObject;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class EndpointGetBazaarItem implements ServiceEndpoint<
        BazaarGetItemProtocolObject.BazaarGetItemMessage,
        BazaarGetItemProtocolObject.BazaarGetItemResponse> {

    @Override
    public BazaarGetItemProtocolObject associatedProtocolObject() {
        return new BazaarGetItemProtocolObject();
    }

    @Override
    public BazaarGetItemProtocolObject.BazaarGetItemResponse onMessage(ServiceProxyRequest message, BazaarGetItemProtocolObject.BazaarGetItemMessage messageObject) {
        String itemName = messageObject.itemName();

        Document document = BazaarService.getCacheService().getItem(itemName);
        BazaarItem item = BazaarItem.fromDocument(document);

        return new BazaarGetItemProtocolObject.BazaarGetItemResponse(item);
    }
}
