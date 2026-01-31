package net.swofty.service.replay.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.replay.ReplayEndProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.ReplayService;
import net.swofty.service.replay.session.ReplaySessionManager;
import org.tinylog.Logger;

import java.util.concurrent.TimeUnit;

public class ReplayEndEndpoint implements ServiceEndpoint<
        ReplayEndProtocolObject.EndMessage,
        ReplayEndProtocolObject.EndResponse> {

    @Override
    public ReplayEndProtocolObject associatedProtocolObject() {
        return new ReplayEndProtocolObject();
    }

    @Override
    public ReplayEndProtocolObject.EndResponse onMessage(
            ServiceProxyRequest message,
            ReplayEndProtocolObject.EndMessage msg) {

        try {
            Logger.info("Ending replay session {} (duration: {} ticks)", msg.replayId(), msg.durationTicks());

            // End session asynchronously but wait for result
            ReplaySessionManager.EndResult result = ReplayService.getSessionManager()
                    .endSession(
                            msg.replayId(),
                            msg.endTime(),
                            msg.durationTicks(),
                            msg.winnerId(),
                            msg.winnerType()
                    )
                    .get(30, TimeUnit.SECONDS); // Wait up to 30 seconds

            Logger.info("Replay {} finalized: {} -> {} bytes",
                    msg.replayId(), result.totalBytes(), result.compressedBytes());

            return new ReplayEndProtocolObject.EndResponse(
                    result.success(),
                    result.totalBytes(),
                    result.compressedBytes(),
                    result.success()
            );

        } catch (Exception e) {
            Logger.error(e, "Failed to end replay session");
            return new ReplayEndProtocolObject.EndResponse(false, 0, 0, false);
        }
    }
}
