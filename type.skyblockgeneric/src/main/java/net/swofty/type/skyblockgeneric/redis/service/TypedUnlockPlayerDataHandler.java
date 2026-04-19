package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.UnlockPlayerDataPushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.skyblockgeneric.redis.service.manager.ServerLockManager;

public class TypedUnlockPlayerDataHandler implements TypedServiceHandler<Request, Response> {

    private static final UnlockPlayerDataPushProtocol PROTOCOL = new UnlockPlayerDataPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        String lockKey = message.playerUUID() + ":" + message.dataKey();
        ServerLockManager.releaseLock(lockKey);
        return Response.success(System.currentTimeMillis());
    }
}
