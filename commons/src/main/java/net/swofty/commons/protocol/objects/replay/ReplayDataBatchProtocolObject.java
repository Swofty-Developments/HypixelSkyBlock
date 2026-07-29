package net.swofty.commons.protocol.objects.replay;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

public class ReplayDataBatchProtocolObject extends RedisProtocol<
        ReplayDataBatchProtocolObject.BatchMessage,
        ReplayDataBatchProtocolObject.BatchResponse> {

    @Override
    public Serializer<BatchMessage> getSerializer() {
        return new JacksonSerializer<>(BatchMessage.class);
    }

    @Override
    public Serializer<BatchResponse> getReturnSerializer() {
        return new JacksonSerializer<>(BatchResponse.class);
    }

    public record BatchMessage(
            UUID replayId,
            int batchIndex,
            int startTick,
            int endTick,
            int recordableCount,
            byte[] compressedData
    ) {}

    public record BatchResponse(boolean success, long bytesReceived) {}
}
