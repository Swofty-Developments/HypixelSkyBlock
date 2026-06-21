package net.swofty.type.generic.redis;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.TeleportProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class RedisTeleport implements RedisMessageHandler<TeleportProtocol.Request, TeleportProtocol.Response> {
    @Override
    public RedisProtocol<TeleportProtocol.Request, TeleportProtocol.Response> protocol() {
        return new TeleportProtocol();
    }

    @Override
    public TeleportProtocol.Response handle(TeleportProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        HypixelPlayer player = HypixelGenericLoader.getFromUUID(uuid);
        if (player == null) return new TeleportProtocol.Response();
        player.teleport(new Pos(message.x(), message.y(), message.z(), message.yaw(), message.pitch()));
        return new TeleportProtocol.Response();
    }
}
