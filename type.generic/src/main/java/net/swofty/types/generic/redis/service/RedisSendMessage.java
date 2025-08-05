package net.swofty.types.generic.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisSendMessage implements ServiceToClient {

    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.SEND_MESSAGE;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        try {
            UUID playerUUID = UUID.fromString(message.getString("playerUUID"));
            String messageText = message.getString("message");

            SkyBlockPlayer player = SkyBlockGenericLoader.getLoadedPlayers().stream()
                    .filter(p -> p.getUuid().equals(playerUUID))
                    .findFirst()
                    .orElse(null);

            if (player != null) {
                player.sendMessage(messageText);
                return new JSONObject().put("success", true);
            }

            return new JSONObject().put("success", false);
        } catch (Exception e) {
            return new JSONObject().put("success", false);
        }
    }
}