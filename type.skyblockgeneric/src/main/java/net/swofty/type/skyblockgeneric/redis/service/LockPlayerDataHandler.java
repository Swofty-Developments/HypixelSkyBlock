package net.swofty.type.skyblockgeneric.redis.service;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol.Request;
import net.swofty.commons.protocol.objects.data.LockPlayerDataPushProtocol.Response;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.redis.service.manager.ServerLockManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.commons.redis.RedisMessageContext;

public class LockPlayerDataHandler implements RedisMessageHandler<Request, Response> {

    private static final LockPlayerDataPushProtocol PROTOCOL = new LockPlayerDataPushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
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
