package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class BazaarProcessPendingTransactionsProtocolObject extends ProtocolObject<
        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage,
        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse> {

    @Override
    public Serializer<BazaarProcessPendingTransactionsMessage> getSerializer() {
        return new JacksonSerializer<>(BazaarProcessPendingTransactionsMessage.class);
    }

    @Override
    public Serializer<BazaarProcessPendingTransactionsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BazaarProcessPendingTransactionsResponse.class);
    }

    public record BazaarProcessPendingTransactionsMessage(UUID playerUUID, UUID profileUUID, List<String> transactionIds) {}

    public record BazaarProcessPendingTransactionsResponse(int processedCount, int failedCount, List<String> successfulTransactionIds, List<String> failedTransactionIds) {}
}
