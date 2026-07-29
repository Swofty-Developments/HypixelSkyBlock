package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarSellProtocol;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointBazaarSellOrder implements RedisMessageHandler<
        BazaarSellProtocol.BazaarSellMessage,
        BazaarSellProtocol.BazaarSellResponse> {

    @Override
    public RedisProtocol protocol() {
        return new BazaarSellProtocol();
    }

    @Override
    public BazaarSellProtocol.BazaarSellResponse handle(BazaarSellProtocol.BazaarSellMessage msg, RedisMessageContext context) {

        String itemName    = msg.itemName();
        UUID   playerUUID  = msg.playerUUID();
        UUID   profileUUID = msg.profileUUID();
        double price       = msg.price();
        double amount      = msg.amount();

        try {
            BazaarMarket.get().submitSell(itemName, playerUUID, profileUUID, price, amount);
            Logger.info("Sell order submitted for {} by {} (profile: {}) — price={}, amount={}",
                    itemName, playerUUID, profileUUID, price, amount);
            return new BazaarSellProtocol.BazaarSellResponse(true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to submit sell order for {} by {}", itemName, playerUUID);
            return new BazaarSellProtocol.BazaarSellResponse(false, "Sell order failed");
        }
    }
}
