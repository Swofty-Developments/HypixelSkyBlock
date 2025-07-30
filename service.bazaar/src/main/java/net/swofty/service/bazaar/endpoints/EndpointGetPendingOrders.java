package net.swofty.service.bazaar.endpoints;

import com.mongodb.client.model.Filters;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocolObject.PendingOrder;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.bazaar.OrderDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EndpointGetPendingOrders implements ServiceEndpoint<
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse> {

    @Override
    public BazaarGetPendingOrdersProtocolObject associatedProtocolObject() {
        return new BazaarGetPendingOrdersProtocolObject();
    }

    @Override
    public BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse onMessage(
            ServiceProxyRequest message,
            BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage msg) {

        UUID player = msg.playerUUID;
        var docs = OrderDatabase.ordersCollection.find(Filters.eq("owner", player.toString()));
        List<PendingOrder> out = new ArrayList<>();

        for (Document d : docs) {
            out.add(new PendingOrder(
                    UUID.fromString(d.getString("_id")),
                    d.getString("itemName"),
                    d.getString("side"),
                    d.getDouble("price"),
                    d.getDouble("remaining")
            ));
        }

        return new BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse(out);
    }
}