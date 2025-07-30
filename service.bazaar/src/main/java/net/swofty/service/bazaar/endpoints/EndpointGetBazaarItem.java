package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocolObject.OrderRecord;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;
import java.util.stream.Collectors;

public class EndpointGetBazaarItem implements ServiceEndpoint<
        BazaarGetItemProtocolObject.BazaarGetItemMessage,
        BazaarGetItemProtocolObject.BazaarGetItemResponse> {

    @Override
    public BazaarGetItemProtocolObject associatedProtocolObject() {
        return new BazaarGetItemProtocolObject();
    }

    @Override
    public BazaarGetItemProtocolObject.BazaarGetItemResponse onMessage(
            ServiceProxyRequest message,
            BazaarGetItemProtocolObject.BazaarGetItemMessage msg) {

        String itemName = msg.itemName();

        // Get current order books from BazaarMarket
        var buyOrders = BazaarMarket.get().getBuyOrders(itemName);
        var sellOrders = BazaarMarket.get().getSellOrders(itemName);

        // Convert to OrderRecord format
        List<OrderRecord> buyOrderRecords = buyOrders.stream()
                .map(order -> new OrderRecord(
                        order.owner,
                        order.price,
                        order.remaining
                ))
                .collect(Collectors.toList());

        List<OrderRecord> sellOrderRecords = sellOrders.stream()
                .map(order -> new OrderRecord(
                        order.owner,
                        order.price,
                        order.remaining
                ))
                .collect(Collectors.toList());

        return new BazaarGetItemProtocolObject.BazaarGetItemResponse(
                itemName,
                buyOrderRecords,
                sellOrderRecords
        );
    }
}