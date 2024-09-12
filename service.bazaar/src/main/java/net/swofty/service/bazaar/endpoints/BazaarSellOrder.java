package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocolObject;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BazaarSellOrder implements ServiceEndpoint<
        BazaarSellProtocolObject.BazaarSellMessage,
        BazaarSellProtocolObject.BazaarSellResponse> {

    @Override
    public ProtocolObject associatedProtocolObject() {
        return new BazaarSellProtocolObject();
    }

    @Override
    public BazaarSellProtocolObject.BazaarSellResponse onMessage(ServiceProxyRequest message, BazaarSellProtocolObject.BazaarSellMessage messageObject) {
        String itemName = messageObject.itemName;
        UUID playerUUID = messageObject.playerUUID;
        Double price = messageObject.price;
        int amount = messageObject.amount;

        Map<String, Object> toReturn = new HashMap<>();

        Document document = BazaarService.getCacheService().getItem(itemName);
        BazaarItem item = BazaarItem.fromDocument(document);

        if (item.getSellOrders().containsKey(playerUUID)) {
            toReturn.put("successful", false);
            return new BazaarSellProtocolObject.BazaarSellResponse(false);
        }

        BazaarService.getCacheService().invalidateCache(itemName);
        item.getSellOrders().put(playerUUID, Map.entry(price, (double) amount));
        BazaarService.getCacheService().setItem(itemName, item.toDocument());

        return new BazaarSellProtocolObject.BazaarSellResponse(true);
    }
}
