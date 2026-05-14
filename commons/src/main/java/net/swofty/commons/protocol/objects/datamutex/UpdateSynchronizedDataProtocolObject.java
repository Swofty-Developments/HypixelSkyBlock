package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class UpdateSynchronizedDataProtocolObject extends ProtocolObject<
        UpdateSynchronizedDataProtocolObject.UpdateDataRequest,
        UpdateSynchronizedDataProtocolObject.UpdateDataResponse> {
    private static final Serializer<UpdateDataRequest> SERIALIZER =
            new JacksonSerializer<>(UpdateDataRequest.class);
    private static final Serializer<UpdateDataResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(UpdateDataResponse.class);

    @Override
    public Serializer<UpdateDataRequest> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<UpdateDataResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record UpdateDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey,
            String newData
    ) {}

    public record UpdateDataResponse(
            boolean success,
            @Nullable String error
    ) {}
}
