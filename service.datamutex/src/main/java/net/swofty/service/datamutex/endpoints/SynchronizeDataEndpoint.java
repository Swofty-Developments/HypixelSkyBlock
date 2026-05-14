package net.swofty.service.datamutex.endpoints;

import org.tinylog.Logger;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.datamutex.SynchronizeDataProtocolObject;
import net.swofty.service.datamutex.DataLockManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SynchronizeDataEndpoint implements ServiceEndpoint<
        SynchronizeDataProtocolObject.SynchronizeDataRequest,
        SynchronizeDataProtocolObject.SynchronizeDataResponse> {

    @Override
    public SynchronizeDataProtocolObject associatedProtocolObject() {
        return new SynchronizeDataProtocolObject();
    }

    @Override
    public SynchronizeDataProtocolObject.SynchronizeDataResponse onMessage(
            ServiceProxyRequest request,
            SynchronizeDataProtocolObject.SynchronizeDataRequest messageObject) {

        List<UUID> serverUUIDs = messageObject.serverUUIDs();
        UUID playerUUID = messageObject.playerUUID();
        String dataKey = messageObject.dataKey();
        String requesterId = request.getRequestServer();
        String lockKey = playerUUID + ":" + dataKey;

        Logger.debug("sync: requester={} player={} key={} servers={} lockKey={}",
                requesterId, playerUUID, dataKey, serverUUIDs, lockKey);

        try {
            if (!DataLockManager.acquireLock(lockKey, requesterId)) {
                Logger.debug("sync: service lock {} already held", lockKey);
                return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                        false, null, "Data is currently locked by another operation");
            }

            Map<UUID, LockPlayerDataPushProtocol.Response> lockResults = ServiceToServerManager
                    .lockPlayerData(serverUUIDs, playerUUID, dataKey)
                    .get();

            boolean allLocked = lockResults.values().stream()
                    .allMatch(LockPlayerDataPushProtocol.Response::success);

            if (!allLocked) {
                Logger.debug("sync: failed to lock all servers (results={}), rolling back",
                        lockResults);
                DataLockManager.releaseLock(lockKey, requesterId);
                ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);
                return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                        false, null, "Failed to acquire locks on all servers");
            }

            Map<UUID, CompletableFuture<GetPlayerDataPushProtocol.Response>> dataFutures = new HashMap<>();
            for (UUID serverUUID : serverUUIDs) {
                dataFutures.put(serverUUID,
                        ServiceToServerManager.getPlayerData(serverUUID, playerUUID, dataKey));
            }

            Map<UUID, GetPlayerDataPushProtocol.Response> allData = new HashMap<>();
            for (Map.Entry<UUID, CompletableFuture<GetPlayerDataPushProtocol.Response>> entry : dataFutures.entrySet()) {
                allData.put(entry.getKey(), entry.getValue().get());
            }

            GetPlayerDataPushProtocol.Response latestData = null;
            long latestTimestamp = 0;
            for (GetPlayerDataPushProtocol.Response data : allData.values()) {
                if (data.success() && data.timestamp() > latestTimestamp) {
                    latestTimestamp = data.timestamp();
                    latestData = data;
                }
            }

            if (latestData == null) {
                Logger.debug("sync: no valid data among {} responses, rolling back", allData.size());
                DataLockManager.releaseLock(lockKey, requesterId);
                ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);
                return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                        false, null, "No valid data found on any server");
            }

            Logger.debug("sync: settled on timestamp={}", latestTimestamp);
            return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                    true, latestData.data(), null);

        } catch (Exception e) {
            Logger.error(e, "Error occurred in data mutex endpoint (lockKey={})", lockKey);
            DataLockManager.releaseLock(lockKey, requesterId);
            ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);
            return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                    false, null, "Error during synchronization: " + e.getMessage());
        }
    }
}
