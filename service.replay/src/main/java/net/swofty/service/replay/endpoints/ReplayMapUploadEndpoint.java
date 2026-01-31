package net.swofty.service.replay.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.replay.ReplayMapUploadProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.ReplayService;
import org.tinylog.Logger;

public class ReplayMapUploadEndpoint implements ServiceEndpoint<
        ReplayMapUploadProtocolObject.MapUploadMessage,
        ReplayMapUploadProtocolObject.MapUploadResponse> {

    @Override
    public ReplayMapUploadProtocolObject associatedProtocolObject() {
        return new ReplayMapUploadProtocolObject();
    }

    @Override
    public ReplayMapUploadProtocolObject.MapUploadResponse onMessage(
            ServiceProxyRequest message,
            ReplayMapUploadProtocolObject.MapUploadMessage msg) {

        try {
            // Check if map already exists
            if (ReplayService.getDatabase().hasMap(msg.mapHash())) {
                Logger.debug("Map {} already exists", msg.mapHash());
                return new ReplayMapUploadProtocolObject.MapUploadResponse(true, true);
            }

            // Save map
            ReplayService.getDatabase().saveMap(msg.mapHash(), msg.mapName(), msg.compressedData());
            Logger.info("Saved map {} ({}) - {} bytes", msg.mapName(), msg.mapHash(), msg.compressedData().length);

            return new ReplayMapUploadProtocolObject.MapUploadResponse(true, false);

        } catch (Exception e) {
            Logger.error(e, "Failed to upload map");
            return new ReplayMapUploadProtocolObject.MapUploadResponse(false, false);
        }
    }
}
