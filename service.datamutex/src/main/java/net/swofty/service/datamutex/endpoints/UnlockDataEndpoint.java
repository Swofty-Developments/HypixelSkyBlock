package net.swofty.service.datamutex.endpoints;

import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.datamutex.UnlockDataProtocolObject;
import net.swofty.service.datamutex.DataLockManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.generic.redis.ServiceToServerManager;

import java.util.List;
import java.util.UUID;

public class UnlockDataEndpoint implements ServiceEndpoint<
        UnlockDataProtocolObject.UnlockDataRequest,
        UnlockDataProtocolObject.UnlockDataResponse> {

    @Override
    public UnlockDataProtocolObject associatedProtocolObject() {
        return new UnlockDataProtocolObject();
    }

    @Override
    public UnlockDataProtocolObject.UnlockDataResponse onMessage(
            ServiceProxyRequest request,
            UnlockDataProtocolObject.UnlockDataRequest messageObject) {

        List<UUID> serverUUIDs = messageObject.serverUUIDs();
        UUID playerUUID = messageObject.playerUUID();
        String dataKey = messageObject.dataKey();
        String requesterId = request.getRequestServer();

        String lockKey = playerUUID + ":" + dataKey;

        try {
            // Release service-level lock
            DataLockManager.releaseLock(lockKey, requesterId);

            ServiceToServerManager.unlockPlayerData(serverUUIDs, playerUUID, dataKey)
                    .thenAccept(results -> {
                        results.forEach((serverUUID, response) -> {
                            if (!response.success()) {
                                System.err.println("Failed to unlock data on server " + serverUUID +
                                        " for player " + playerUUID + ", dataKey: " + dataKey);
                            }
                        });
                    })
                    .exceptionally(throwable -> {
                        System.err.println("Error unlocking data on servers: " + throwable.getMessage());
                        return null;
                    });

            return new UnlockDataProtocolObject.UnlockDataResponse(
                    true, null);

        } catch (Exception e) {
            return new UnlockDataProtocolObject.UnlockDataResponse(
                    false, "Error during unlock: " + e.getMessage());
        }
    }
}