package net.swofty.type.generic.redis;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.GivePlayersOriginTypeProtocol;
import net.swofty.proxyapi.redis.TypedProxyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisOriginServer implements TypedProxyHandler<GivePlayersOriginTypeProtocol.Request, GivePlayersOriginTypeProtocol.Response> {
    public static Map<UUID, ServerType> origin = new HashMap<>();

    @Override
    public ProtocolObject<GivePlayersOriginTypeProtocol.Request, GivePlayersOriginTypeProtocol.Response> getProtocol() {
        return new GivePlayersOriginTypeProtocol();
    }

    @Override
    public GivePlayersOriginTypeProtocol.Response onMessage(GivePlayersOriginTypeProtocol.Request message) {
        UUID uuid = UUID.fromString(message.uuid());
        ServerType originType = ServerType.valueOf(message.originType());
        origin.put(uuid, originType);
        return new GivePlayersOriginTypeProtocol.Response();
    }
}
