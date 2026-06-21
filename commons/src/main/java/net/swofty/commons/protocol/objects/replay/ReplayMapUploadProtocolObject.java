package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

public class ReplayMapUploadProtocolObject extends RedisProtocol<
        ReplayMapUploadProtocolObject.MapUploadMessage,
        ReplayMapUploadProtocolObject.MapUploadResponse> {

    @Override
    public Serializer<MapUploadMessage> getSerializer() {
        return new JacksonSerializer<>(MapUploadMessage.class);
    }

    @Override
    public Serializer<MapUploadResponse> getReturnSerializer() {
        return new JacksonSerializer<>(MapUploadResponse.class);
    }

    public record MapUploadMessage(
            String mapHash,
            String mapName,
            byte[] compressedData
    ) {}

    public record MapUploadResponse(boolean success, boolean alreadyExists) {}
}
