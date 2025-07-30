package net.swofty.service.bazaar;

import net.swofty.commons.bazaar.BazaarTransaction;
import net.swofty.commons.bazaar.SuccessfulBazaarTransaction;
import net.swofty.commons.bazaar.OrderExpiredBazaarTransaction;

public class BazaarPropagator {
    public void propagate(BazaarTransaction tx) {
        System.out.println("Propagating transaction " + tx.getClass().getSimpleName());
        // Called for every match or expiration.
        // e.g. if (tx instanceof SuccessfulBazaarTransaction s) { credit buyer, debit seller, log trade }
        //      if (tx instanceof OrderExpiredBazaarTransaction e) { return items/coins, notify player }
    }
}
