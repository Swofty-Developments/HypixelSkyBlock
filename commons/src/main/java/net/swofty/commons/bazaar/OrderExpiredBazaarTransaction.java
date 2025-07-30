package net.swofty.commons.bazaar;

import java.time.Instant;
import java.util.UUID;

public record OrderExpiredBazaarTransaction(
        UUID     orderId,
        String   itemName,
        UUID     owner,
        String   side,          // "BUY" or "SELL"
        double   remainingQty,
        Instant  expiredAt
) implements BazaarTransaction {}
