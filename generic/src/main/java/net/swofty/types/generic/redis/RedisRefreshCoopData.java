package net.swofty.types.generic.redis;

import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

public class RedisRefreshCoopData implements ProxyToClient {
    @Override
    public String onMessage(String message) {
        UUID uuid = UUID.fromString(message.split(",")[0]);
        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);

        if (player == null) return "ok";

        String datapoint = message.split(",")[1];

        DataHandler dataHandler = DataHandler.fromDocument(
                new ProfilesDatabase(player.getProfiles().getCurrentlySelected().toString()).getDocument()
        );

        player.getDataHandler().getDatapoint(datapoint).setValueBypassCoop(
                dataHandler.getDatapoint(datapoint).getValue()
        );

        Document toReplace = player.getDataHandler().toDocument(player.getProfiles().getCurrentlySelected());
        ProfilesDatabase.replaceDocument(player.getProfiles().getCurrentlySelected().toString(), toReplace);

        return "ok";
    }
}
