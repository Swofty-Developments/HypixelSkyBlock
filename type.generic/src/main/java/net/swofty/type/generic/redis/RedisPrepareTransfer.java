package net.swofty.type.generic.redis;

import net.swofty.commons.data.SwoftyData;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.PrepareTransferProtocol;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.mongodb.UserDatabase;

import java.util.UUID;

public class RedisPrepareTransfer implements RedisMessageHandler<PrepareTransferProtocol.Request, PrepareTransferProtocol.Response> {

    @Override
    public RedisProtocol<PrepareTransferProtocol.Request, PrepareTransferProtocol.Response> protocol() {
        return new PrepareTransferProtocol();
    }

    @Override
    public PrepareTransferProtocol.Response handle(PrepareTransferProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        SwoftyData.account().load(uuid);

        if (HypixelConst.getTypeLoader().getType().isSkyBlock()) {
            SkyBlockPlayerProfiles profiles = new UserDatabase(uuid).getProfiles();
            UUID profileId = profiles.getCurrentlySelected();
            if (profileId != null) SwoftyData.profile().load(profileId);
        }

        return new PrepareTransferProtocol.Response(true);
    }
}
