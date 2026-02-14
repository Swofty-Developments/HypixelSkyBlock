package net.swofty.service.replay.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.replay.ReplayMapLoadProtocolObject;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.replay.ReplayService;
import org.tinylog.Logger;

public class ReplayMapLoadEndpoint implements ServiceEndpoint<
    ReplayMapLoadProtocolObject.MapLoadRequest,
    ReplayMapLoadProtocolObject.MapLoadResponse> {
    @Override
    public ReplayMapLoadProtocolObject associatedProtocolObject() {
        return new ReplayMapLoadProtocolObject();
    }

    @Override
    public ReplayMapLoadProtocolObject.MapLoadResponse onMessage(
        ServiceProxyRequest message,
        ReplayMapLoadProtocolObject.MapLoadRequest msg) {
        try {
            String mapHash = msg.mapHash();
            if (!ReplayService.getDatabase().hasMap(mapHash)) {
                Logger.debug("Map not found: {}", mapHash);
                return new ReplayMapLoadProtocolObject.MapLoadResponse(
                    true, false, null, null, 0
                );
            }
            byte[] compressedData = ReplayService.getDatabase().getMapData(mapHash);
            if (compressedData == null) {
                Logger.warn("Map data is null for hash: {}", mapHash);
                return new ReplayMapLoadProtocolObject.MapLoadResponse(
                    false, false, null, null, 0
                );
            }
            Logger.info("Loaded map {} - {} bytes", mapHash, compressedData.length);
            return new ReplayMapLoadProtocolObject.MapLoadResponse(
                true, true, mapHash, compressedData, 0
            );
        } catch (Exception e) {
            Logger.error(e, "Failed to load map {}", msg.mapHash());
            return new ReplayMapLoadProtocolObject.MapLoadResponse(
                false, false, null, null, 0
            );
        }
    }
}
