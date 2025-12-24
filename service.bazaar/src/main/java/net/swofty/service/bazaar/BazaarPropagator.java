package net.swofty.service.bazaar;

import net.swofty.commons.skyblock.bazaar.BazaarTransaction;
import net.swofty.commons.skyblock.bazaar.SuccessfulBazaarTransaction;
import net.swofty.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public class BazaarPropagator {

    public void propagate(BazaarTransaction tx) {
        System.out.println("Propagating transaction " + tx.getClass().getSimpleName());

        // Send transaction to all servers
        JSONObject message = new JSONObject();
        message.put("type", tx.getClass().getSimpleName());
        message.put("data", tx.toJSON());

        // Send to all servers and collect responses with 5 second timeout
        ServiceToServerManager.sendToAllServers(
                        FromServiceChannels.PROPAGATE_BAZAAR_TRANSACTION,
                        message,
                        5000 // 5 second timeout
                ).thenAccept(responses -> handleServerResponses(tx, responses))
                .exceptionally(throwable -> {
                    System.err.println("Failed to get responses from servers for transaction: " + throwable.getMessage());
                    return null;
                });
    }

    private void handleServerResponses(BazaarTransaction tx, Map<UUID, JSONObject> responses) {
        switch (tx) {
            case SuccessfulBazaarTransaction success -> handleSuccessfulTransactionResponses(success, responses);
            case OrderExpiredBazaarTransaction expired -> handleExpiredTransactionResponses(expired, responses);
            default -> System.err.println("Unknown transaction type for response handling: " + tx.getClass().getSimpleName());
        }
    }

    private void handleSuccessfulTransactionResponses(SuccessfulBazaarTransaction tx, Map<UUID, JSONObject> responses) {
        boolean buyerHandled = false;
        boolean sellerHandled = false;

        // Check if any server successfully handled the buyer or seller
        for (Map.Entry<UUID, JSONObject> entry : responses.entrySet()) {
            JSONObject response = entry.getValue();
            if (response != null && response.optBoolean("success", false)) {
                buyerHandled |= response.optBoolean("buyerHandled", false);
                sellerHandled |= response.optBoolean("sellerHandled", false);
            }
        }

        // Store pending transactions for players not handled by any server
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

    private void handleExpiredTransactionResponses(OrderExpiredBazaarTransaction tx, Map<UUID, JSONObject> responses) {
        boolean ownerHandled = false;

        // Check if any server successfully handled the owner
        for (Map.Entry<UUID, JSONObject> entry : responses.entrySet()) {
            JSONObject response = entry.getValue();
            if (response != null && response.optBoolean("success", false)) {
                ownerHandled |= response.optBoolean("ownerHandled", false);
            }
        }

        // Store pending transaction for owner if not handled by any server
        if (!ownerHandled) {
            System.out.println("Owner " + tx.owner() + " not handled by any server - storing as pending");
            PendingTransactionsDatabase.storePendingTransaction(tx.owner(), tx.ownerProfile(), tx);
        }

        System.out.println("Expired order handled - Owner: " + ownerHandled);
    }
}