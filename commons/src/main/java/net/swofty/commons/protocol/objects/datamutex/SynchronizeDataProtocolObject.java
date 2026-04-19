package net.swofty.commons.protocol.objects.datamutex;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

public class SynchronizeDataProtocolObject extends ProtocolObject<
        SynchronizeDataProtocolObject.SynchronizeDataRequest,
        SynchronizeDataProtocolObject.SynchronizeDataResponse> {

    @Override
    public Serializer<SynchronizeDataRequest> getSerializer() {
        return new JacksonSerializer<>(SynchronizeDataRequest.class);
    }

    @Override
    public Serializer<SynchronizeDataResponse> getReturnSerializer() {
        return new JacksonSerializer<>(SynchronizeDataResponse.class);
    }

    public record SynchronizeDataRequest(
            List<UUID> serverUUIDs,
            UUID playerUUID,
            String dataKey
    ) {}

    public record SynchronizeDataResponse(
            boolean success,
            String message,
            String synchronizedData
    ) {}
}
