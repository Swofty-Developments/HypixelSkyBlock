package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class SynchronizeDataProtocolObject extends ProtocolObject<
        SynchronizeDataProtocolObject.SynchronizeDataRequest,
        SynchronizeDataProtocolObject.SynchronizeDataResponse> {
    private static final Serializer<SynchronizeDataRequest> SERIALIZER =
            new JacksonSerializer<>(SynchronizeDataRequest.class);
    private static final Serializer<SynchronizeDataResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(SynchronizeDataResponse.class);

    @Override
    public Serializer<SynchronizeDataRequest> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<SynchronizeDataResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record SynchronizeDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey
    ) {}

    public record SynchronizeDataResponse(
            boolean success,
            String synchronizedData,
            @Nullable String error
    ) {}
}
