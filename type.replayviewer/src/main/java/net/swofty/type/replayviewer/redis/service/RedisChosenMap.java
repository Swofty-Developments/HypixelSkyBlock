package net.swofty.type.replayviewer.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisChosenMap implements ServiceToClient {
    public static Map<UUID, String> replay = new HashMap<>();

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.VIEW_REPLAY;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        String gameId = message.getString("replay-id");

        replay.put(uuid, gameId);
        return new JSONObject();
    }
}