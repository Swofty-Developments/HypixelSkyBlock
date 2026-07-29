package net.swofty.service.replay.endpoints;

import net.swofty.commons.protocol.objects.replay.ReplayDataBatchProtocolObject;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.service.replay.ReplayService;
import org.tinylog.Logger;

public class ReplayDataBatchEndpoint implements RedisMessageHandler<
        ReplayDataBatchProtocolObject.BatchMessage,
        ReplayDataBatchProtocolObject.BatchResponse> {

    @Override
    public ReplayDataBatchProtocolObject protocol() {
        return new ReplayDataBatchProtocolObject();
    }

    @Override
    public ReplayDataBatchProtocolObject.BatchResponse handle(ReplayDataBatchProtocolObject.BatchMessage msg, RedisMessageContext context) {

        try {
            ReplayService.getSessionManager().receiveBatch(
                    msg.replayId(),
                    msg.batchIndex(),
                    msg.startTick(),
                    msg.endTick(),
                    msg.recordableCount(),
                    msg.compressedData()
            );

            Logger.debug("Received batch {} for replay {} ({} bytes, ticks {}-{})",
                    msg.batchIndex(), msg.replayId(), msg.compressedData().length, msg.startTick(), msg.endTick());

            return new ReplayDataBatchProtocolObject.BatchResponse(true, msg.compressedData().length);

        } catch (Exception e) {
            Logger.error(e, "Failed to process replay batch");
            return new ReplayDataBatchProtocolObject.BatchResponse(false, 0);
        }
    }
}
