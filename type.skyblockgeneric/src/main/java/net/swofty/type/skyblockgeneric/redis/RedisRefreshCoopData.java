package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
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

        DataHandler dataHandler = DataHandler.fromDocument(
                new ProfilesDatabase(player.getProfiles().getCurrentlySelected().toString()).getDocument()
        );

        player.getDataHandler().getDatapoint(datapoint).setValueBypassCoop(
                dataHandler.getDatapoint(datapoint).getValue()
        );

        Document toReplace = player.getDataHandler().toDocument(player.getProfiles().getCurrentlySelected());
        ProfilesDatabase.replaceDocument(player.getProfiles().getCurrentlySelected().toString(), toReplace);

        return new JSONObject();
    }
}
