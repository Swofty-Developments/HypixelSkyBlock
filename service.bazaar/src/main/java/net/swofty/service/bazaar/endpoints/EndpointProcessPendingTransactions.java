package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse;
import net.swofty.service.bazaar.PendingTransactionsDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.ArrayList;
import java.util.List;

public class EndpointProcessPendingTransactions implements ServiceEndpoint<
        BazaarProcessPendingTransactionsMessage,
        BazaarProcessPendingTransactionsResponse> {

    @Override
    public BazaarProcessPendingTransactionsProtocolObject associatedProtocolObject() {
        return new BazaarProcessPendingTransactionsProtocolObject();
    }

    @Override
    public BazaarProcessPendingTransactionsResponse onMessage(
            ServiceProxyRequest message,
            BazaarProcessPendingTransactionsMessage msg) {

        List<String> successfulIds = new ArrayList<>();
        List<String> failedIds = new ArrayList<>();

        System.out.println("Processing " + msg.transactionIds.size() +
                " pending transactions for player " + msg.playerUUID);

        for (String transactionId : msg.transactionIds) {
            try {
                // Mark the transaction as processed in the database
                PendingTransactionsDatabase.markTransactionProcessed(transactionId);
                successfulIds.add(transactionId);
                System.out.println("Successfully processed pending transaction: " + transactionId);
            } catch (Exception e) {
                failedIds.add(transactionId);
                System.err.println("Failed to process pending transaction " + transactionId + ": " + e.getMessage());
            }
        }

        // Clean up processed transactions
        if (!successfulIds.isEmpty()) {
            try {
                PendingTransactionsDatabase.cleanupProcessedTransactions();
            } catch (Exception e) {
                System.err.println("Failed to cleanup processed transactions: " + e.getMessage());
            }
        }

        return new BazaarProcessPendingTransactionsResponse(
                successfulIds.size(),
                failedIds.size(),
                successfulIds,
                failedIds
        );
    }
}