package net.swofty.service.bazaar.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsResponse;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocolObject.PendingTransactionInfo;
import net.swofty.service.bazaar.PendingTransactionsDatabase;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;
import java.util.stream.Collectors;

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

        // Get pending transactions from database
        List<PendingTransactionsDatabase.PendingTransaction> pendingTransactions =
                PendingTransactionsDatabase.getPendingTransactions(msg.playerUUID, msg.profileUUID);

        // Convert to protocol format
        List<PendingTransactionInfo> transactionInfos = pendingTransactions.stream()
                .map(pt -> new PendingTransactionInfo(
                        pt.getId(),
                        pt.getTransaction().getClass().getSimpleName(),
                        pt.getTransaction().toJSON(),
                        pt.getCreatedAt()
                ))
                .collect(Collectors.toList());

        System.out.println("Retrieved " + transactionInfos.size() +
                " pending transactions for player " + msg.playerUUID +
                " on profile " + msg.profileUUID);

        return new BazaarGetPendingTransactionsResponse(transactionInfos);
    }
}