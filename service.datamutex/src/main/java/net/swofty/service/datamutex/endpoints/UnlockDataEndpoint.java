package net.swofty.service.datamutex.endpoints;

import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.datamutex.UnlockDataProtocol;
import net.swofty.service.datamutex.DataLockManager;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.redis.RedisClient;
import org.tinylog.Logger;

import java.util.List;
import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class UnlockDataEndpoint implements RedisMessageHandler<
        UnlockDataProtocol.UnlockDataRequest,
        UnlockDataProtocol.UnlockDataResponse> {

    @Override
    public UnlockDataProtocol protocol() {
        return new UnlockDataProtocol();
    }

    @Override
    public UnlockDataProtocol.UnlockDataResponse handle(UnlockDataProtocol.UnlockDataRequest messageObject, RedisMessageContext context) {

        List<UUID> serverUUIDs = messageObject.serverUUIDs();
        UUID playerUUID = messageObject.playerUUID();
        String dataKey = messageObject.dataKey();
        String requesterId = context.origin().id();

        String lockKey = playerUUID + ":" + dataKey;

        try {
            // Release service-level lock
            DataLockManager.releaseLock(lockKey, requesterId);

            RedisClient.requestServersFromService(serverUUIDs, new UnlockPlayerDataPushProtocol(),
                            new UnlockPlayerDataPushProtocol.Request(playerUUID, dataKey))
                    .thenAccept(results -> results.forEach((serverUUID, response) -> {
                        if (!response.success()) {
                            Logger.warn("Failed to unlock data on server {} for player {}, dataKey: {}",
                                    serverUUID, playerUUID, dataKey);
                        }
                    }))
                    .exceptionally(throwable -> {
                        Logger.error(throwable, "Error unlocking data on servers");
                        return null;
                    });

            return new UnlockDataProtocol.UnlockDataResponse(
                    true, null);

        } catch (Exception e) {
            return new UnlockDataProtocol.UnlockDataResponse(
                    false, "Error during unlock: " + e.getMessage());
        }
    }
}
