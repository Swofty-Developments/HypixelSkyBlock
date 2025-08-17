package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.service.FromServiceChannels;
import net.swofty.proxyapi.redis.ServiceToClient;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.json.JSONObject;

import java.util.UUID;

public class RedisUpdatePlayerData implements ServiceToClient {
    @Override
    public FromServiceChannels getChannel() {
        return FromServiceChannels.UPDATE_PLAYER_DATA;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID playerUUID = UUID.fromString(message.getString("playerUUID"));
        String dataKey = message.getString("dataKey");
        String newDataSerialized = message.getString("newData");

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(playerUUID);
        if (player == null) {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Player not found on this server");
        }

        try {
            HypixelDataHandler.Data dataType = HypixelDataHandler.Data.fromKey(dataKey);
            if (dataType == null) {
                return new JSONObject()
                        .put("success", false)
                        .put("error", "Invalid data key: " + dataKey);
            }

            // Deserialize and set the new data
            Datapoint datapoint = dataType.getDefaultDatapoint().getClass().getDeclaredConstructor(String.class).newInstance(dataKey);
            datapoint.deserializeValue(newDataSerialized);
            datapoint.setUser(player.getSkyblockDataHandler()).setData(dataType);

            player.getSkyblockDataHandler().getDatapoints().put(dataKey, datapoint);

            return new JSONObject()
                    .put("success", true)
                    .put("timestamp", System.currentTimeMillis());

        } catch (Exception e) {
            return new JSONObject()
                    .put("success", false)
                    .put("error", "Failed to update data: " + e.getMessage());
        }
    }
}