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

    @Override
    public Serializer<UnlockDataRequest> getSerializer() {
        return new JacksonSerializer<>(UnlockDataRequest.class);
    }

    @Override
    public Serializer<UnlockDataResponse> getReturnSerializer() {
        return new JacksonSerializer<>(UnlockDataResponse.class);
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
