package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocol.BazaarProcessPendingTransactionsMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarProcessPendingTransactionsProtocol.BazaarProcessPendingTransactionsResponse;
import net.swofty.service.bazaar.PendingTransactionsDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointProcessPendingTransactions implements RedisMessageHandler<
        BazaarProcessPendingTransactionsMessage,
        BazaarProcessPendingTransactionsResponse> {

    @Override
    public BazaarProcessPendingTransactionsProtocol protocol() {
        return new BazaarProcessPendingTransactionsProtocol();
    }

    @Override
    public BazaarProcessPendingTransactionsResponse handle(BazaarProcessPendingTransactionsMessage msg, RedisMessageContext context) {

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
