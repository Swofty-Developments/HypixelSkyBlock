package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class ReplayEndProtocolObject extends RedisProtocol<
        ReplayEndProtocolObject.EndMessage,
        ReplayEndProtocolObject.EndResponse> {

    @Override
    public Serializer<EndMessage> getSerializer() {
        return new JacksonSerializer<>(EndMessage.class);
    }

    @Override
    public Serializer<EndResponse> getReturnSerializer() {
        return new JacksonSerializer<>(EndResponse.class);
    }

    public record EndMessage(
            UUID replayId,
            long endTime,
            int durationTicks
    ) {}

    public record EndResponse(
            boolean success,
            long totalBytes,
            long compressedBytes,
            boolean available
    ) {}
}
