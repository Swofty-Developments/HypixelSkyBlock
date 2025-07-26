package net.swofty.types.generic.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.types.generic.redis.service.manager.ServerLockManager;
import org.json.JSONObject;

import java.util.UUID;

public class RedisUnlockPlayerData implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.UNLOCK_PLAYER_DATA;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID playerUUID = UUID.fromString(message.getString("playerUUID"));
        String dataKey = message.getString("dataKey");

        String lockKey = playerUUID + ":" + dataKey;

        ServerLockManager.releaseLock(lockKey);

        return new JSONObject()
                .put("success", true)
                .put("unlockTime", System.currentTimeMillis());
    }
}