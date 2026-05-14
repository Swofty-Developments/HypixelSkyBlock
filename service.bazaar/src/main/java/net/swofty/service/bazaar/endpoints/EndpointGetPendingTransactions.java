package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsResponse;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject.PendingTransactionInfo;
import net.swofty.service.bazaar.PendingTransactionsDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.tinylog.Logger;

import java.util.List;

public class EndpointGetPendingTransactions implements ServiceEndpoint<
        BazaarGetPendingTransactionsMessage,
        BazaarGetPendingTransactionsResponse> {

    @Override
    public BazaarGetPendingTransactionsProtocolObject associatedProtocolObject() {
        return new BazaarGetPendingTransactionsProtocolObject();
    }

    @Override
    public BazaarGetPendingTransactionsResponse onMessage(
            ServiceProxyRequest message,
            BazaarGetPendingTransactionsMessage msg) {

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
