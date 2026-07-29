package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocol.OrderRecord;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.List;
import java.util.stream.Collectors;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointGetBazaarItem implements RedisMessageHandler<
        BazaarGetItemProtocol.BazaarGetItemMessage,
        BazaarGetItemProtocol.BazaarGetItemResponse> {

    @Override
    public BazaarGetItemProtocol protocol() {
        return new BazaarGetItemProtocol();
    }

    @Override
    public BazaarGetItemProtocol.BazaarGetItemResponse handle(BazaarGetItemProtocol.BazaarGetItemMessage msg, RedisMessageContext context) {

        String itemName = msg.itemName();

        // Get current order books from BazaarMarket
        var buyOrders = BazaarMarket.get().getBuyOrders(itemName);
        var sellOrders = BazaarMarket.get().getSellOrders(itemName);

        // Convert to OrderRecord format
        List<OrderRecord> buyOrderRecords = buyOrders.stream()
                .map(order -> new OrderRecord(
                        order.owner,
                        order.profileUuid,
                        order.originalPrice,
                        order.remaining
                ))
                .collect(Collectors.toList());

        List<OrderRecord> sellOrderRecords = sellOrders.stream()
                .map(order -> new OrderRecord(
                        order.owner,
                        order.profileUuid,
                        order.originalPrice,
                        order.remaining
                ))
                .collect(Collectors.toList());

        return new BazaarGetItemProtocol.BazaarGetItemResponse(
                itemName,
                buyOrderRecords,
                sellOrderRecords,
                true,
                null
        );
    }
}