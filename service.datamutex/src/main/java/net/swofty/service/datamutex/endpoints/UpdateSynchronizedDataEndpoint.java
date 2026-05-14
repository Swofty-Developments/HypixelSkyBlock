package net.swofty.service.datamutex.endpoints;

import org.tinylog.Logger;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.data.UpdatePlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.datamutex.UpdateSynchronizedDataProtocolObject;
import net.swofty.service.datamutex.DataLockManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UpdateSynchronizedDataEndpoint implements ServiceEndpoint<
        UpdateSynchronizedDataProtocolObject.UpdateDataRequest,
        UpdateSynchronizedDataProtocolObject.UpdateDataResponse> {

    @Override
    public UpdateSynchronizedDataProtocolObject associatedProtocolObject() {
        return new UpdateSynchronizedDataProtocolObject();
    }

    @Override
    public UpdateSynchronizedDataProtocolObject.UpdateDataResponse onMessage(
            ServiceProxyRequest request,
            UpdateSynchronizedDataProtocolObject.UpdateDataRequest messageObject) {

        List<UUID> serverUUIDs = messageObject.serverUUIDs();
        UUID playerUUID = messageObject.playerUUID();
        String dataKey = messageObject.dataKey();
        String newData = messageObject.newData();
        String requesterId = request.getRequestServer();
        String lockKey = playerUUID + ":" + dataKey;

        Logger.debug("update: requester={} player={} key={} servers={} bytes={} lockKey={}",
                requesterId, playerUUID, dataKey, serverUUIDs, newData.length(), lockKey);

        try {
            DataLockManager.LockInfo lockInfo = DataLockManager.getLockInfo(lockKey);
            if (lockInfo == null || !lockInfo.requesterId().equals(requesterId)) {
                Logger.debug("update: lock check failed (held by {})",
                        lockInfo == null ? "<none>" : lockInfo.requesterId());
                return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                        false, "Lock has expired or is held by another requester");
            }

            Map<UUID, CompletableFuture<UpdatePlayerDataPushProtocol.Response>> updateFutures = new HashMap<>();
            for (UUID serverUUID : serverUUIDs) {
                updateFutures.put(serverUUID,
                        ServiceToServerManager.updatePlayerData(serverUUID, playerUUID, dataKey, newData));
            }

            Map<UUID, UpdatePlayerDataPushProtocol.Response> updateResults = new HashMap<>();
            for (Map.Entry<UUID, CompletableFuture<UpdatePlayerDataPushProtocol.Response>> entry : updateFutures.entrySet()) {
                updateResults.put(entry.getKey(), entry.getValue().get());
            }

            boolean allUpdated = updateResults.values().stream()
                    .allMatch(UpdatePlayerDataPushProtocol.Response::success);

            if (!allUpdated) {
                Logger.warn("update: partial failure (results={})", updateResults);
                return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                        false, "Failed to update data on all servers");
            }

            return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(true, null);

        } catch (Exception e) {
            Logger.error(e, "Error occurred in data mutex update endpoint (lockKey={})", lockKey);
            return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                    false, "Error during data update: " + e.getMessage());
        } finally {
            DataLockManager.releaseLock(lockKey, requesterId);
            ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);
        }
    }
}
