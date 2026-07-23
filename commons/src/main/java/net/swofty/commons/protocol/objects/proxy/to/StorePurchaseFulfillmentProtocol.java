package net.swofty.commons.protocol.objects.proxy.to;

import net.swofty.commons.protocol.RedisProtocol;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StorePurchaseFulfillmentProtocol
    extends RedisProtocol<StorePurchaseFulfillmentProtocol.Request, StorePurchaseFulfillmentProtocol.Response> {

    public StorePurchaseFulfillmentProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(
        String purchaseId,
        String playerUuid,
        String playerName,
        String productId,
        String productName,
        long paidAt,
        List<Entitlement> entitlements
    ) {
    }

    public record Entitlement(
        String type,
        String key,
        long amount,
        @Nullable Long durationDays
    ) {
    }

    public record Response(boolean success, boolean duplicate, @Nullable String error) {
    }
}
