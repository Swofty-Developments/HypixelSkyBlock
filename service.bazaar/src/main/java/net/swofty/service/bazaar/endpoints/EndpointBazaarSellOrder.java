package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocolObject;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.UUID;

public class EndpointBazaarSellOrder implements ServiceEndpoint<
        BazaarSellProtocolObject.BazaarSellMessage,
        BazaarSellProtocolObject.BazaarSellResponse> {

    @Override
    public ProtocolObject associatedProtocolObject() {
        return new BazaarSellProtocolObject();
    }

    @Override
    public BazaarSellProtocolObject.BazaarSellResponse onMessage(
            ServiceProxyRequest message,
            BazaarSellProtocolObject.BazaarSellMessage msg) {

        String itemName    = msg.itemName;
        UUID   playerUUID  = msg.playerUUID;
        UUID   profileUUID = msg.profileUUID;
        double price       = msg.price;
        double amount      = msg.amount;

        try {
            BazaarMarket.get().submitSell(itemName, playerUUID, profileUUID, price, amount);
            System.out.println("Sell order submitted for " + itemName + " by " + playerUUID
                    + " (profile: " + profileUUID + ") - Price: " + price + ", Amount: " + amount);
            return new BazaarSellProtocolObject.BazaarSellResponse(true);
        } catch (Exception e) {
            System.err.println("Failed to submit sell order: " + e.getMessage());
            return new BazaarSellProtocolObject.BazaarSellResponse(false);
        }
    }
}