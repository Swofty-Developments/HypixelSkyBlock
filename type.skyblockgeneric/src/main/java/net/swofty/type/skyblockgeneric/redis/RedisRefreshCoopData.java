package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;
import org.json.JSONObject;

import java.util.UUID;

public class RedisRefreshCoopData implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.REFRESH_COOP_DATA_ON_SERVER;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        String datapoint = message.getString("datapoint");

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);
        if (player == null) return new JSONObject();

        SkyBlockDataHandler dataHandler = SkyBlockDataHandler.createFromProfileOnly(
                new ProfilesDatabase(player.getProfiles().getCurrentlySelected().toString()).getDocument()
        );

        @SuppressWarnings("unchecked")
        SkyBlockDatapoint<Object> targetDatapoint = (SkyBlockDatapoint<Object>) player.getSkyblockDataHandler().getSkyBlockDatapoint(datapoint);
        Object value = dataHandler.getSkyBlockDatapoint(datapoint).getValue();
        targetDatapoint.setValueBypassCoop(value); // starting to remind me of Python - ArikSquad

        Document toReplace = player.getSkyblockDataHandler().toProfileDocument();
        ProfilesDatabase.replaceDocument(player.getProfiles().getCurrentlySelected().toString(), toReplace);

        return new JSONObject();
    }
}
