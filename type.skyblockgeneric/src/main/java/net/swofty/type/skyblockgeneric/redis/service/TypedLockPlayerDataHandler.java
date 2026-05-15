package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.ServicePushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol.Response;
import net.swofty.proxyapi.redis.TypedServiceHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.redis.service.manager.ServerLockManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class TypedLockPlayerDataHandler implements TypedServiceHandler<Request, Response> {

    private static final LockPlayerDataPushProtocol PROTOCOL = new LockPlayerDataPushProtocol();

    @Override
    public ServicePushProtocol<Request, Response> getProtocol() {
        return PROTOCOL;
    }

    @Override
    public Response onMessage(Request message) {
        SkyBlockPlayer player = SkyBlockGenericLoader.getFromUUID(message.playerUUID());
        if (player == null) {
            return Response.failure("Player not found on this server");
        }

        String lockKey = message.playerUUID() + ":" + message.dataKey();

        if (ServerLockManager.acquireLock(lockKey)) {
            return Response.success(System.currentTimeMillis());
        } else {
            return Response.failure("Data is already locked");
        }
    }
}
