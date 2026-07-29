package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.RefreshCoopDataProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class RedisRefreshCoopData implements RedisMessageHandler<RefreshCoopDataProtocol.Request, RefreshCoopDataProtocol.Response> {
    @Override
    public RedisProtocol<RefreshCoopDataProtocol.Request, RefreshCoopDataProtocol.Response> protocol() {
        return new RefreshCoopDataProtocol();
    }

    @Override
    public RefreshCoopDataProtocol.Response handle(RefreshCoopDataProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        String datapoint = message.datapoint();

        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(uuid);
        if (player == null) return new RefreshCoopDataProtocol.Response();

        SkyBlockDataHandler dataHandler = SkyBlockDataHandler.createFromProfileOnly(
                new ProfilesDatabase(player.getProfiles().getCurrentlySelected().toString()).getDocument()
        );

        @SuppressWarnings("unchecked")
        SkyBlockDatapoint<Object> targetDatapoint = (SkyBlockDatapoint<Object>) player.getSkyblockDataHandler().getSkyBlockDatapoint(datapoint);
        Object value = dataHandler.getSkyBlockDatapoint(datapoint).getValue();
        targetDatapoint.setValueBypassCoop(value);

        Document toReplace = player.getSkyblockDataHandler().toProfileDocument();
        ProfilesDatabase.replaceDocument(player.getProfiles().getCurrentlySelected().toString(), toReplace);

        return new RefreshCoopDataProtocol.Response();
    }
}
