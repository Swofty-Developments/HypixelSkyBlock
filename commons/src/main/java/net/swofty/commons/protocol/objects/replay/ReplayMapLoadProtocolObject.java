package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

public class ReplayMapLoadProtocolObject extends RedisProtocol<
    ReplayMapLoadProtocolObject.MapLoadRequest,
    ReplayMapLoadProtocolObject.MapLoadResponse> {
    @Override
    public Serializer<MapLoadRequest> getSerializer() {
        return new JacksonSerializer<>(MapLoadRequest.class);
    }

    @Override
    public Serializer<MapLoadResponse> getReturnSerializer() {
        return new JacksonSerializer<>(MapLoadResponse.class);
    }

    public record MapLoadRequest(String mapHash) {
    }

    public record MapLoadResponse(
        boolean success,
        boolean found,
        String mapHash,
        byte[] compressedData,
        int uncompressedSize
    ) {
    }
}
