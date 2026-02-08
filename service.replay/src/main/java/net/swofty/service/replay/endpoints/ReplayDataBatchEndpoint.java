package net.swofty.service.replay.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.replay.ReplayDataBatchProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.ReplayService;
import org.tinylog.Logger;

public class ReplayDataBatchEndpoint implements ServiceEndpoint<
        ReplayDataBatchProtocolObject.BatchMessage,
        ReplayDataBatchProtocolObject.BatchResponse> {

    @Override
    public ReplayDataBatchProtocolObject associatedProtocolObject() {
        return new ReplayDataBatchProtocolObject();
    }

    @Override
    public ReplayDataBatchProtocolObject.BatchResponse onMessage(
            ServiceProxyRequest message,
            ReplayDataBatchProtocolObject.BatchMessage msg) {

        try {
            ReplayService.getSessionManager().receiveBatch(
                    msg.replayId(),
                    msg.batchIndex(),
                    msg.startTick(),
                    msg.endTick(),
                    msg.recordableCount(),
                    msg.data()
            );

            Logger.debug("Received batch {} for replay {} ({} bytes, ticks {}-{})",
                    msg.batchIndex(), msg.replayId(), msg.data().length, msg.startTick(), msg.endTick());

            return new ReplayDataBatchProtocolObject.BatchResponse(true, msg.data().length);

        } catch (Exception e) {
            Logger.error(e, "Failed to process replay batch");
            return new ReplayDataBatchProtocolObject.BatchResponse(false, 0);
        }
    }
}
