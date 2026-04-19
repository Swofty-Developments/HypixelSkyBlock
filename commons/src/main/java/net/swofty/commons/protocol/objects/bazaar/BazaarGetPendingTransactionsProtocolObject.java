package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BazaarGetPendingTransactionsProtocolObject extends ProtocolObject<
        BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsMessage,
        BazaarGetPendingTransactionsProtocolObject.BazaarGetPendingTransactionsResponse> {

    @Override
    public Serializer<BazaarGetPendingTransactionsMessage> getSerializer() {
        return new JacksonSerializer<>(BazaarGetPendingTransactionsMessage.class);
    }

    @Override
    public Serializer<BazaarGetPendingTransactionsResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BazaarGetPendingTransactionsResponse.class);
    }

    public record BazaarGetPendingTransactionsMessage(UUID playerUUID, UUID profileUUID) {}

    public record BazaarGetPendingTransactionsResponse(List<PendingTransactionInfo> transactions) {}

    public record PendingTransactionInfo(
            String id,
            String transactionType,
            Map<String, Object> transactionData,
            Instant createdAt
    ) {}
}
