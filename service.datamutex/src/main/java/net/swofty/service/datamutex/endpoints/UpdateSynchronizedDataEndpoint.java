// Replace your UpdateSynchronizedDataEndpoint with this debug version
package net.swofty.service.datamutex.endpoints;

import org.tinylog.Logger;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.datamutex.UpdateSynchronizedDataProtocolObject;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.datamutex.DataLockManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;
import org.json.JSONObject;

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

        System.out.println("=== UPDATE ENDPOINT DEBUG ===");
        System.out.println("Received update request from: " + request.getRequestServer());
        System.out.println("Player UUID: " + messageObject.playerUUID());
        System.out.println("Data Key: " + messageObject.dataKey());
        System.out.println("Server UUIDs: " + messageObject.serverUUIDs());
        System.out.println("New Data Length: " + messageObject.newData().length() + " chars");

        List<UUID> serverUUIDs = messageObject.serverUUIDs();
        UUID playerUUID = messageObject.playerUUID();
        String dataKey = messageObject.dataKey();
        String newData = messageObject.newData();
        String requesterId = request.getRequestServer();

        String lockKey = playerUUID + ":" + dataKey;
        System.out.println("Lock key: " + lockKey);

        try {
            // Verify we still hold the lock
            System.out.println("Verifying service lock...");
            DataLockManager.LockInfo lockInfo = DataLockManager.getLockInfo(lockKey);
            if (lockInfo == null || !lockInfo.requesterId.equals(requesterId)) {
                System.out.println("Lock verification failed - lockInfo: " + lockInfo + ", requesterId: " + requesterId);
                return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                        false, "Lock has expired or is held by another requester");
            }
            System.out.println("Service lock verified successfully");

            // Step 1: Update data on all servers
            System.out.println("Updating data on servers: " + serverUUIDs);
            Map<UUID, CompletableFuture<JSONObject>> updateFutures = new java.util.HashMap<>();
            for (UUID serverUUID : serverUUIDs) {
                JSONObject updateMessage = new JSONObject()
                        .put("playerUUID", playerUUID.toString())
                        .put("dataKey", dataKey)
                        .put("newData", newData);

                System.out.println("Sending update to server " + serverUUID + ": " + updateMessage);

                updateFutures.put(serverUUID,
                        ServiceToServerManager.sendToServer(serverUUID, FromServiceChannels.UPDATE_PLAYER_DATA, updateMessage));
            }

            // Wait for all updates
            System.out.println("Waiting for update responses...");
            Map<UUID, JSONObject> updateResults = new java.util.HashMap<>();
            for (Map.Entry<UUID, CompletableFuture<JSONObject>> entry : updateFutures.entrySet()) {
                JSONObject result = entry.getValue().get();
                updateResults.put(entry.getKey(), result);
                System.out.println("Update result from " + entry.getKey() + ": " + result);
            }

            // Check if all updates were successful
            boolean allUpdated = updateResults.values().stream()
                    .allMatch(result -> result.optBoolean("success", false));

            System.out.println("All servers updated successfully: " + allUpdated);

            if (!allUpdated) {
                System.out.println("Some updates failed, returning error");
                return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                        false, "Failed to update data on all servers");
            }

            System.out.println("All updates successful!");
            return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                    true, "Data successfully synchronized across all servers");

        } catch (Exception e) {
            System.out.println("Exception in update endpoint: " + e.getMessage());
            Logger.error(e, "Error occurred in data mutex endpoint");

            return new UpdateSynchronizedDataProtocolObject.UpdateDataResponse(
                    false, "Error during data update: " + e.getMessage());
        } finally {
            // Always release locks when done
            System.out.println("Releasing locks in finally block...");
            DataLockManager.releaseLock(lockKey, requesterId);
            ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey);
        }
    }
}