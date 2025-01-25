package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarBuyProtocolObject;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class BazaarBuyOrder implements ServiceEndpoint<
        BazaarBuyProtocolObject.BazaarBuyMessage,
        BazaarBuyProtocolObject.BazaarBuyResponse> {

    @Override
    public BazaarBuyProtocolObject associatedProtocolObject() {
        return new BazaarBuyProtocolObject();
    }

    @Override
    public BazaarBuyProtocolObject.BazaarBuyResponse onMessage(ServiceProxyRequest message, BazaarBuyProtocolObject.BazaarBuyMessage messageObject) {
        String itemName = messageObject.itemName;
        UUID playerUUID = messageObject.playerUUID;
        int price = messageObject.price;
        int amount = messageObject.amount;

        Document document = BazaarService.getCacheService().getItem(itemName);
        BazaarItem item = BazaarItem.fromDocument(document);

        if (item.getBuyOrders().containsKey(playerUUID)) {
            return new BazaarBuyProtocolObject.BazaarBuyResponse(false);
        }

        BazaarService.getCacheService().invalidateCache(itemName);
        item.getBuyOrders().put(playerUUID, Map.entry((double) price, (double) amount));
        BazaarService.getCacheService().setItem(itemName, item.toDocument());

        return new BazaarBuyProtocolObject.BazaarBuyResponse(true);
    }
}
