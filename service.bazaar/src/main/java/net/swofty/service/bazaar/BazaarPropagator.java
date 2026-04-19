package net.swofty.service.bazaar;

import net.swofty.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol.Response;
import net.swofty.commons.skyblock.bazaar.BazaarTransaction;
import net.swofty.commons.skyblock.bazaar.SuccessfulBazaarTransaction;
import net.swofty.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import net.swofty.service.generic.redis.ServiceToServerManager;

import java.util.Map;
import java.util.UUID;

public class BazaarPropagator {

    private static final BazaarTransactionPushProtocol PROTOCOL = new BazaarTransactionPushProtocol();

    public void propagate(BazaarTransaction tx) {
        System.out.println("Propagating transaction " + tx.getClass().getSimpleName());

        var request = new BazaarTransactionPushProtocol.Request(
                tx.getClass().getSimpleName(),
                tx.toJSON().toString()
        );

        ServiceToServerManager.sendToAllServers(PROTOCOL, request, 5000)
                .thenAccept(responses -> handleServerResponses(tx, responses))
                .exceptionally(throwable -> {
                    System.err.println("Failed to get responses from servers for transaction: " + throwable.getMessage());
                    return null;
                });
    }

    private void handleServerResponses(BazaarTransaction tx, Map<UUID, Response> responses) {
        switch (tx) {
            case SuccessfulBazaarTransaction success -> handleSuccessfulTransactionResponses(success, responses);
            case OrderExpiredBazaarTransaction expired -> handleExpiredTransactionResponses(expired, responses);
            default -> System.err.println("Unknown transaction type for response handling: " + tx.getClass().getSimpleName());
        }
    }

    private void handleSuccessfulTransactionResponses(SuccessfulBazaarTransaction tx, Map<UUID, Response> responses) {
        boolean buyerHandled = false;
        boolean sellerHandled = false;

        for (Map.Entry<UUID, Response> entry : responses.entrySet()) {
            Response response = entry.getValue();
            if (response != null && response.success()) {
                buyerHandled |= response.buyerHandled();
                sellerHandled |= response.sellerHandled();
            }
        }

        if (!buyerHandled) {
            System.out.println("Buyer " + tx.buyer() + " not handled by any server - storing as pending");
            PendingTransactionsDatabase.storePendingTransaction(tx.buyer(), tx.buyerProfile(), tx);
        }

        if (!sellerHandled) {
            System.out.println("Seller " + tx.seller() + " not handled by any server - storing as pending");
            PendingTransactionsDatabase.storePendingTransaction(tx.seller(), tx.sellerProfile(), tx);
        }

        System.out.println("Transaction handled - Buyer: " + buyerHandled + ", Seller: " + sellerHandled);
    }

    private void handleExpiredTransactionResponses(OrderExpiredBazaarTransaction tx, Map<UUID, Response> responses) {
        boolean ownerHandled = false;

        for (Map.Entry<UUID, Response> entry : responses.entrySet()) {
            Response response = entry.getValue();
            if (response != null && response.success()) {
                ownerHandled |= response.buyerHandled();
            }
        }

        if (!ownerHandled) {
            System.out.println("Owner " + tx.owner() + " not handled by any server - storing as pending");
            PendingTransactionsDatabase.storePendingTransaction(tx.owner(), tx.ownerProfile(), tx);
        }

        System.out.println("Expired order handled - Owner: " + ownerHandled);
    }
}