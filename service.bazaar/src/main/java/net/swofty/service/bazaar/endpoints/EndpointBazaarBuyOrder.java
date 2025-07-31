package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarBuyProtocolObject;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.service.generic.redis.ServiceEndpoint;

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

        String itemName    = msg.itemName;
        UUID   playerUUID  = msg.playerUUID;
        UUID   profileUUID = msg.profileUUID;
        double price       = msg.price;
        double amount      = msg.amount;

        try {
            BazaarMarket.get().submitBuy(itemName, playerUUID, profileUUID, price, amount);
            System.out.println("Buy order submitted for " + itemName + " by " + playerUUID
                    + " (profile: " + profileUUID + ") - Price: " + price + ", Amount: " + amount);
            return new BazaarBuyProtocolObject.BazaarBuyResponse(true);
        } catch (Exception e) {
            System.err.println("Failed to submit buy order: " + e.getMessage());
            return new BazaarBuyProtocolObject.BazaarBuyResponse(false);
        }
    }
}
