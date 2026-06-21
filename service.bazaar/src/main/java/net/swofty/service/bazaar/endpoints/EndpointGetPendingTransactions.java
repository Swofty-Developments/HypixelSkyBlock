package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol.BazaarGetPendingTransactionsMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol.BazaarGetPendingTransactionsResponse;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol.PendingTransactionInfo;
import net.swofty.service.bazaar.PendingTransactionsDatabase;
import net.swofty.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.List;
import net.swofty.commons.redis.RedisMessageContext;

public class EndpointGetPendingTransactions implements RedisMessageHandler<
        BazaarGetPendingTransactionsMessage,
        BazaarGetPendingTransactionsResponse> {

    @Override
    public BazaarGetPendingTransactionsProtocol protocol() {
        return new BazaarGetPendingTransactionsProtocol();
    }

    @Override
    public BazaarGetPendingTransactionsResponse handle(BazaarGetPendingTransactionsMessage msg, RedisMessageContext context) {

        List<PendingTransactionsDatabase.PendingTransaction> pendingTransactions =
                PendingTransactionsDatabase.getPendingTransactions(msg.playerUUID(), msg.profileUUID());

        List<PendingTransactionInfo> transactionInfos = pendingTransactions.stream()
                .map(pt -> new PendingTransactionInfo(
                        pt.getId(),
                        pt.getTransaction().getClass().getSimpleName(),
                        pt.getTransaction().toJSON().toMap(),
                        pt.getCreatedAt()
                ))
                .toList();

        Logger.debug("Retrieved {} pending transactions for player {} on profile {}",
                transactionInfos.size(), msg.playerUUID(), msg.profileUUID());

        return new BazaarGetPendingTransactionsResponse(transactionInfos, true, null);
    }
}
