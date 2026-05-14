package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocolObject;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

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

        String itemName    = msg.itemName();
        UUID   playerUUID  = msg.playerUUID();
        UUID   profileUUID = msg.profileUUID();
        double price       = msg.price();
        double amount      = msg.amount();

        try {
            BazaarMarket.get().submitSell(itemName, playerUUID, profileUUID, price, amount);
            Logger.info("Sell order submitted for {} by {} (profile: {}) — price={}, amount={}",
                    itemName, playerUUID, profileUUID, price, amount);
            return new BazaarSellProtocolObject.BazaarSellResponse(true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to submit sell order for {} by {}", itemName, playerUUID);
            return new BazaarSellProtocolObject.BazaarSellResponse(false, "Sell order failed");
        }
    }
}
