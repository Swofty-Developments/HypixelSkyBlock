package net.swofty.type.generic.redis;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.TeleportProtocol;
import net.swofty.proxyapi.redis.TypedProxyHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.UUID;

public class RedisTeleport implements TypedProxyHandler<TeleportProtocol.Request, TeleportProtocol.Response> {
    @Override
    public ProtocolObject<TeleportProtocol.Request, TeleportProtocol.Response> getProtocol() {
        return new TeleportProtocol();
    }

    @Override
    public TeleportProtocol.Response onMessage(TeleportProtocol.Request message) {
        UUID uuid = UUID.fromString(message.uuid());
        HypixelPlayer player = HypixelGenericLoader.getFromUUID(uuid);
        if (player == null) return new TeleportProtocol.Response();
        player.teleport(new Pos(message.x(), message.y(), message.z(), message.yaw(), message.pitch()));
        return new TeleportProtocol.Response();
    }
}
