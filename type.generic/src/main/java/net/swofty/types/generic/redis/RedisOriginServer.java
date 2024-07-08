package net.swofty.types.generic.redis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisOriginServer implements ProxyToClient {
    public static Map<UUID, ServerType> origin = new HashMap<>();

    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        ServerType originType = ServerType.valueOf(message.getString("origin-type"));

        origin.put(uuid, originType);
        return new JSONObject();
    }
}
