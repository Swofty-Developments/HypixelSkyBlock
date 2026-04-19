package net.swofty.service.datamutex.endpoints;

import org.tinylog.Logger;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.data.GetPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.datamutex.SynchronizeDataProtocolObject;
import net.swofty.service.datamutex.DataLockManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;

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

        System.out.println("=== SYNC ENDPOINT DEBUG ===");
        System.out.println("Received sync request from: " + request.getRequestServer());
        System.out.println("Player UUID: " + messageObject.playerUUID());
        System.out.println("Data Key: " + messageObject.dataKey());
        System.out.println("Server UUIDs: " + messageObject.serverUUIDs());

        List<UUID> serverUUIDs = messageObject.serverUUIDs();
        UUID playerUUID = messageObject.playerUUID();
        String dataKey = messageObject.dataKey();
        String requesterId = request.getRequestServer();

        String lockKey = playerUUID + ":" + dataKey;
        System.out.println("Lock key: " + lockKey);

        try {
            // Step 1: Acquire service-level lock
            System.out.println("Attempting to acquire service lock...");
            if (!DataLockManager.acquireLock(lockKey, requesterId)) {
                System.out.println("Failed to acquire service lock - already locked");
                return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                        false, null, "Data is currently locked by another operation");
            }
            System.out.println("Service lock acquired successfully");

            // Step 2: Lock data on all servers
            System.out.println("Locking data on servers: " + serverUUIDs);
            Map<UUID, LockPlayerDataPushProtocol.Response> lockResults = ServiceToServerManager
                    .lockPlayerData(serverUUIDs, playerUUID, dataKey)
                    .get();

            System.out.println("Lock results: " + lockResults);

            boolean allLocked = lockResults.values().stream()
                    .allMatch(LockPlayerDataPushProtocol.Response::success);

            System.out.println("All servers locked: " + allLocked);

            if (!allLocked) {
                // Release service lock and any server locks we did get
                System.out.println("Not all servers locked, cleaning up...");
                DataLockManager.releaseLock(lockKey, requesterId);
                ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);

                return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                        false, null, "Failed to acquire locks on all servers");
            }

            // Step 3: Get data from all servers
            System.out.println("Getting data from all servers...");
            Map<UUID, CompletableFuture<GetPlayerDataPushProtocol.Response>> dataFutures = new java.util.HashMap<>();
            for (UUID serverUUID : serverUUIDs) {
                dataFutures.put(serverUUID,
                        ServiceToServerManager.getPlayerData(serverUUID, playerUUID, dataKey));
            }

            Map<UUID, GetPlayerDataPushProtocol.Response> allData = new java.util.HashMap<>();
            for (Map.Entry<UUID, CompletableFuture<GetPlayerDataPushProtocol.Response>> entry : dataFutures.entrySet()) {
                allData.put(entry.getKey(), entry.getValue().get());
            }

            System.out.println("Received data from servers: " + allData);

            GetPlayerDataPushProtocol.Response latestData = null;
            long latestTimestamp = 0;

            for (GetPlayerDataPushProtocol.Response data : allData.values()) {
                System.out.println("Processing data response: " + data);
                if (data.success()) {
                    long timestamp = data.timestamp();
                    System.out.println("Data timestamp: " + timestamp);
                    if (timestamp > latestTimestamp) {
                        latestTimestamp = timestamp;
                        latestData = data;
                    }
                }
            }

            if (latestData == null) {
                System.out.println("No valid data found, cleaning up...");
                DataLockManager.releaseLock(lockKey, requesterId);
                ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);

                return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                        false, null, "No valid data found on any server");
            }

            System.out.println("Using latest data with timestamp: " + latestTimestamp);
            System.out.println("Latest data content: " + latestData.data());

            return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                    true, latestData.data(), null);

        } catch (Exception e) {
            System.out.println("Exception in sync endpoint: " + e.getMessage());
            Logger.error(e, "Error occurred in data mutex endpoint");

            // Always unlock on error
            DataLockManager.releaseLock(lockKey, requesterId);
            ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);

            return new SynchronizeDataProtocolObject.SynchronizeDataResponse(
                    false, null, "Error during synchronization: " + e.getMessage());
        }
    }
}