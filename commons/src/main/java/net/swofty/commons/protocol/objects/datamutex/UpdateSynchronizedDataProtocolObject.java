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

    @Override
    public Serializer<UpdateDataRequest> getSerializer() {
        return new JacksonSerializer<>(UpdateDataRequest.class);
    }

    @Override
    public Serializer<UpdateDataResponse> getReturnSerializer() {
        return new JacksonSerializer<>(UpdateDataResponse.class);
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
