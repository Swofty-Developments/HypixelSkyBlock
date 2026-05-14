package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse;
import net.swofty.service.bazaar.PendingTransactionsDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

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

        Logger.info("Processing {} pending transactions for player {}",
                msg.transactionIds().size(), msg.playerUUID());

        for (String transactionId : msg.transactionIds()) {
            try {
                PendingTransactionsDatabase.markTransactionProcessed(transactionId);
                successfulIds.add(transactionId);
                Logger.debug("Successfully processed pending transaction: {}", transactionId);
            } catch (Exception e) {
                failedIds.add(transactionId);
                Logger.error(e, "Failed to process pending transaction {}", transactionId);
            }
        }

        if (!successfulIds.isEmpty()) {
            try {
                PendingTransactionsDatabase.cleanupProcessedTransactions();
            } catch (Exception e) {
                Logger.error(e, "Failed to cleanup processed transactions");
            }
        }

        return new BazaarProcessPendingTransactionsResponse(
                successfulIds.size(),
                failedIds.size(),
                successfulIds,
                failedIds,
                true,
                null
        );
    }
}
