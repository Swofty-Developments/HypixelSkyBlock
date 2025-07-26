package net.swofty.types.generic.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisGetPlayerData implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.GET_PLAYER_DATA;
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

        try {
            DataHandler.Data dataType = DataHandler.Data.fromKey(dataKey);
            if (dataType == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", "Invalid data key: " + dataKey);
            }

            Object data = player.getDataHandler().get(dataType, dataType.getType()).getValue();
            String serializedData = dataType.getDefaultDatapoint().getSerializer().serialize(data);

            return new JSONObject()
                    .put("success", true)
                    .put("data", serializedData)
                    .put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Failed to get data: " + e.getMessage());
        }
    }
}