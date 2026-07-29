package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BazaarCancelProtocol extends RedisProtocol<
        BazaarCancelProtocol.CancelMessage,
        BazaarCancelProtocol.CancelResponse> {
    private static final Serializer<CancelMessage> SERIALIZER =
            new JacksonSerializer<>(CancelMessage.class);
    private static final Serializer<CancelResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(CancelResponse.class);

    @Override
    public Serializer<CancelMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<CancelResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record CancelMessage(UUID orderId, UUID playerUuid, UUID profileUuid) {}

    public record CancelResponse(boolean success, @Nullable String error) {}
}
