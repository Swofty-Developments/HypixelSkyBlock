package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.bazaar.BazaarService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BazaarBuyOrder implements ServiceEndpoint {
    @Override
    public String channel() {
        return "bazaar-buy-order";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        String itemName = (String) messageData.get("item-name");
        UUID playerUUID = (UUID) messageData.get("player-uuid");
        int price = (int) messageData.get("price");
        int amount = (int) messageData.get("amount");

        Map<String, Object> toReturn = new HashMap<>();

        Document document = BazaarService.getCacheService().getItem(itemName);
        BazaarItem item = BazaarItem.fromDocument(document);

        if (item.getBuyOrders().containsKey(playerUUID)) {
            toReturn.put("successful", false);
            return toReturn;
        }

        BazaarService.getCacheService().invalidateCache(itemName);
        item.getBuyOrders().put(playerUUID, Map.entry((double) price, (double) amount));
        BazaarService.getCacheService().setItem(itemName, item.toDocument());

        return toReturn;
    }
}
