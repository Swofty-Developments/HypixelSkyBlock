package net.swofty.commons.bazaar;

import java.time.Instant;
import java.util.UUID;

// Fired when a buy+sell actually crosses
public record SuccessfulBazaarTransaction(
        String   itemName,
        UUID     buyer,
        UUID     seller,
        double   pricePerUnit,
        double   quantity,
        double   taxTaken,      // coins withheld by the system
        Instant timestamp
) implements BazaarTransaction {}
