package net.swofty.service.bazaar.endpoints;

import com.mongodb.client.model.Filters;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject.CancelMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject.CancelResponse;
import net.swofty.service.bazaar.OrderDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

public class EndpointCancelBazaarOrder implements ServiceEndpoint<
        CancelMessage, CancelResponse> {

    @Override
    public BazaarCancelProtocolObject associatedProtocolObject() {
        return new BazaarCancelProtocolObject();
    }

    @Override
    public CancelResponse onMessage(ServiceProxyRequest _msg, CancelMessage msg) {
        // Remove the order if it belongs to this player
        var result = OrderDatabase.ordersCollection.deleteOne(
                Filters.and(
                        Filters.eq("_id", msg.orderId.toString()),
                        Filters.eq("owner", msg.playerUuid.toString())
                )
        );

        boolean success = result.getDeletedCount() > 0;
        return new CancelResponse(success);
    }
}