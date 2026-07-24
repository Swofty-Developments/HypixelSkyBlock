package net.swofty.type.generic.redis;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.PrepareTransferProtocol;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.domain.PlayerDataService;

import java.util.UUID;

public class RedisPrepareTransfer implements RedisMessageHandler<PrepareTransferProtocol.Request, PrepareTransferProtocol.Response> {

    @Override
    public RedisProtocol<PrepareTransferProtocol.Request, PrepareTransferProtocol.Response> protocol() {
        return new PrepareTransferProtocol();
    }

    @Override
    public PrepareTransferProtocol.Response handle(PrepareTransferProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        ServerType originType = message.originType() != null ? ServerType.valueOf(message.originType()) : null;
        if (originType != null) RedisOriginServer.origin.put(uuid, originType);
        PlayerDataService.prepare(HypixelConst.getTypeLoader().getType(), uuid, originType);
        return new PrepareTransferProtocol.Response(true);
    }
}
