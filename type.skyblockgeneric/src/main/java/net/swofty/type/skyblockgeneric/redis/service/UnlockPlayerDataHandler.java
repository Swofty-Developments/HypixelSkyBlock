package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol.Response;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.skyblockgeneric.redis.service.manager.ServerLockManager;
import net.swofty.commons.redis.RedisMessageContext;

public class UnlockPlayerDataHandler implements RedisMessageHandler<Request, Response> {

    private static final UnlockPlayerDataPushProtocol PROTOCOL = new UnlockPlayerDataPushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
        String lockKey = message.playerUUID() + ":" + message.dataKey();
        ServerLockManager.releaseLock(lockKey);
        return Response.success(System.currentTimeMillis());
    }
}
