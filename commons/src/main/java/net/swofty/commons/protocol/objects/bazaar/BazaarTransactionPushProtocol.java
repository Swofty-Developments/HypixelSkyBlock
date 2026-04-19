package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.ServicePushProtocol;
import org.jetbrains.annotations.Nullable;

public class BazaarTransactionPushProtocol
        extends ServicePushProtocol<BazaarTransactionPushProtocol.Request, BazaarTransactionPushProtocol.Response> {

    public BazaarTransactionPushProtocol() {
        super(Request.class, Response.class);
    }

    public record Request(String transactionType, String transactionJson) {}

    public record Response(boolean success, boolean buyerHandled, boolean sellerHandled, String transactionId, @Nullable String error) {
        public static Response failure(String reason) {
            return new Response(false, false, false, null, reason);
        }
    }
}
