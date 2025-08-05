package net.swofty.service.bazaar.endpoints;

import com.mongodb.client.model.Filters;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject.CancelMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocolObject.CancelResponse;
import net.swofty.service.bazaar.BazaarMarket;
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
        // Remove the order if it belongs to this player and profile
        var result = OrderDatabase.ordersCollection.deleteOne(
                Filters.and(
                        Filters.eq("_id", msg.orderId.toString()),
                        Filters.eq("owner", msg.playerUuid.toString()),
                        Filters.eq("profileUuid", msg.profileUuid.toString())  // Also check profile
                )
        );
        BazaarMarket.get().submitDelete(msg.orderId, msg.playerUuid, msg.profileUuid);
        System.out.println("Deleted order " + msg.orderId + " for player " + msg.playerUuid + " and profile " + msg.profileUuid);

        boolean success = result.getDeletedCount() > 0;
        return new CancelResponse(success);
    }
}