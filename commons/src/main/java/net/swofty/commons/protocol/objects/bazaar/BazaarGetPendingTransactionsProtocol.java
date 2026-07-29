package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class BazaarGetPendingTransactionsProtocol extends RedisProtocol<
        BazaarGetPendingTransactionsProtocol.BazaarGetPendingTransactionsMessage,
        BazaarGetPendingTransactionsProtocol.BazaarGetPendingTransactionsResponse> {
    private static final Serializer<BazaarGetPendingTransactionsMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarGetPendingTransactionsMessage.class);
    private static final Serializer<BazaarGetPendingTransactionsResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarGetPendingTransactionsResponse.class);

    @Override
    public Serializer<BazaarGetPendingTransactionsMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BazaarGetPendingTransactionsResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record BazaarGetPendingTransactionsMessage(UUID playerUUID, UUID profileUUID) {}

    public record BazaarGetPendingTransactionsResponse(List<PendingTransactionInfo> transactions, boolean success, @Nullable String error) {}

    public record PendingTransactionInfo(
            String id,
            String transactionType,
            Map<String, Object> transactionData,
            Instant createdAt
    ) {}
}
