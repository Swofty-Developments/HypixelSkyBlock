package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class BazaarProcessPendingTransactionsProtocolObject extends ProtocolObject<
        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsMessage,
        BazaarProcessPendingTransactionsProtocolObject.BazaarProcessPendingTransactionsResponse> {
    private static final Serializer<BazaarProcessPendingTransactionsMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarProcessPendingTransactionsMessage.class);
    private static final Serializer<BazaarProcessPendingTransactionsResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarProcessPendingTransactionsResponse.class);

    @Override
    public Serializer<BazaarProcessPendingTransactionsMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BazaarProcessPendingTransactionsResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record BazaarProcessPendingTransactionsMessage(UUID playerUUID, UUID profileUUID, List<String> transactionIds) {}

    public record BazaarProcessPendingTransactionsResponse(int processedCount, int failedCount, List<String> successfulTransactionIds, List<String> failedTransactionIds, boolean success, @Nullable String error) {}
}
