package net.swofty.service.bazaar.endpoints;

import com.mongodb.client.model.Filters;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocol.CancelMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarCancelProtocol.CancelResponse;
import net.swofty.service.bazaar.BazaarMarket;
import net.swofty.service.bazaar.OrderDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointCancelBazaarOrder implements RedisMessageHandler<
        CancelMessage, CancelResponse> {

    @Override
    public BazaarCancelProtocol protocol() {
        return new BazaarCancelProtocol();
    }

    @Override
    public CancelResponse handle(CancelMessage msg, RedisMessageContext context) {
        var result = OrderDatabase.ordersCollection.deleteOne(
                Filters.and(
                        Filters.eq("_id", msg.orderId().toString()),
                        Filters.eq("owner", msg.playerUuid().toString()),
                        Filters.eq("profileUuid", msg.profileUuid().toString())
                )
        );
        BazaarMarket.get().submitDelete(msg.orderId(), msg.playerUuid(), msg.profileUuid());
        Logger.info("Deleted order {} for player {} and profile {}",
                msg.orderId(), msg.playerUuid(), msg.profileUuid());

        boolean success = result.getDeletedCount() > 0;
        return new CancelResponse(success, success ? null : "Cancel failed");
    }
}
