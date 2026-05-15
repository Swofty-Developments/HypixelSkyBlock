package net.swofty.service.bazaar.endpoints;

import com.mongodb.client.model.Filters;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocol.PendingOrder;
import net.swofty.service.bazaar.OrderDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointGetPendingOrders implements RedisMessageHandler<
        BazaarGetPendingOrdersProtocol.BazaarGetPendingOrdersMessage,
        BazaarGetPendingOrdersProtocol.BazaarGetPendingOrdersResponse> {

    @Override
    public BazaarGetPendingOrdersProtocol protocol() {
        return new BazaarGetPendingOrdersProtocol();
    }

    @Override
    public BazaarGetPendingOrdersProtocol.BazaarGetPendingOrdersResponse handle(BazaarGetPendingOrdersProtocol.BazaarGetPendingOrdersMessage msg, RedisMessageContext context) {

        UUID player = msg.playerUUID();
        UUID profile = msg.profileUUID();

        var docs = OrderDatabase.ordersCollection.find(
                Filters.and(
                        Filters.eq("owner", player.toString()),
                        Filters.eq("profileUuid", profile.toString())
                )
        );
        List<PendingOrder> out = new ArrayList<>();

        for (Document d : docs) {
            out.add(new PendingOrder(
                    UUID.fromString(d.getString("_id")),
                    d.getString("itemName"),
                    d.getString("side"),
                    d.getDouble("originalPrice"),
                    d.getDouble("remaining"),
                    UUID.fromString(d.getString("profileUuid"))
            ));
        }

        return new BazaarGetPendingOrdersProtocol.BazaarGetPendingOrdersResponse(out, true, null);
    }
}
