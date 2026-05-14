package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class UnlockDataProtocolObject extends ProtocolObject<
        UnlockDataProtocolObject.UnlockDataRequest,
        UnlockDataProtocolObject.UnlockDataResponse> {
    private static final Serializer<UnlockDataRequest> SERIALIZER =
            new JacksonSerializer<>(UnlockDataRequest.class);
    private static final Serializer<UnlockDataResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(UnlockDataResponse.class);

    @Override
    public Serializer<UnlockDataRequest> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<UnlockDataResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record UnlockDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey
    ) {}

    public record UnlockDataResponse(
            boolean success,
            @Nullable String error
    ) {}
}
