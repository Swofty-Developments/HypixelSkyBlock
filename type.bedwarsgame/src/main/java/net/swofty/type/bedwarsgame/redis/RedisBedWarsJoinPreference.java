package net.swofty.type.bedwarsgame.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RedisBedWarsJoinPreference implements ProxyToClient {

    public record Preference(String mode, String map) {}

    public static final Map<UUID, Preference> preferences = new ConcurrentHashMap<>();

    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.BEDWARS_JOIN_PREFERENCE;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        String mode = message.optString("mode", "");
        String map = message.optString("map", "");
        preferences.put(uuid, new Preference(mode, map));
        return new JSONObject();
    }
}
