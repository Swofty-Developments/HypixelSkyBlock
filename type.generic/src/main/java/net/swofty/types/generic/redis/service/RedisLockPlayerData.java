package net.swofty.types.generic.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.redis.service.manager.ServerLockManager;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisLockPlayerData implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.LOCK_PLAYER_DATA;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID playerUUID = UUID.fromString(message.getString("playerUUID"));
        String dataKey = message.getString("dataKey");

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(playerUUID);
        if (player == null) {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Player not found on this server");
        }

        String lockKey = playerUUID + ":" + dataKey;

        if (ServerLockManager.acquireLock(lockKey)) {
            return new JSONObject()
                    .put("success", true)
                    .put("lockTime", System.currentTimeMillis());
        } else {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Data is already locked");
        }
    }
}