package net.swofty.type.generic.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisGameMessage implements ServiceToClient {
	public static Map<UUID, String> game = new HashMap<>();

	@Override
	public FromServiceChannels getChannel() {
		return FromServiceChannels.GAME_INFORMATION;
	}

	@Override
	public JSONObject onMessage(JSONObject message) {
		UUID uuid = UUID.fromString(message.getString("uuid"));
		String gameId = message.getString("game-id");

		game.put(uuid, gameId);
		return new JSONObject();
	}
}
