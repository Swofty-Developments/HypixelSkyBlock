package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarBuyProtocolObject;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.UUID;

public class EndpointBazaarBuyOrder implements ServiceEndpoint<
        BazaarBuyProtocolObject.BazaarBuyMessage,
        BazaarBuyProtocolObject.BazaarBuyResponse> {

    @Override
    public BazaarBuyProtocolObject associatedProtocolObject() {
        return new BazaarBuyProtocolObject();
    }

    @Override
    public BazaarBuyProtocolObject.BazaarBuyResponse onMessage(
            ServiceProxyRequest message,
            BazaarBuyProtocolObject.BazaarBuyMessage msg) {

        String itemName    = msg.itemName();
        UUID   playerUUID  = msg.playerUUID();
        UUID   profileUUID = msg.profileUUID();
        double price       = msg.price();
        double amount      = msg.amount();

        try {
            BazaarMarket.get().submitBuy(itemName, playerUUID, profileUUID, price, amount);
            Logger.info("Buy order submitted for {} by {} (profile: {}) — price={}, amount={}",
                    itemName, playerUUID, profileUUID, price, amount);
            return new BazaarBuyProtocolObject.BazaarBuyResponse(true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to submit buy order for {} by {}", itemName, playerUUID);
            return new BazaarBuyProtocolObject.BazaarBuyResponse(false, "Buy order failed");
        }
    }
}
