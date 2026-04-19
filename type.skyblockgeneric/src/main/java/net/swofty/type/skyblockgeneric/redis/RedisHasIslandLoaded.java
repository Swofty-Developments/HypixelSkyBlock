package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.DoesServerHaveIslandProtocol;
import net.swofty.proxyapi.redis.TypedProxyHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockIsland;

import java.util.UUID;

public class RedisHasIslandLoaded implements TypedProxyHandler<DoesServerHaveIslandProtocol.Request, DoesServerHaveIslandProtocol.Response> {
    @Override
    public ProtocolObject<DoesServerHaveIslandProtocol.Request, DoesServerHaveIslandProtocol.Response> getProtocol() {
        return new DoesServerHaveIslandProtocol();
    }

    @Override
    public DoesServerHaveIslandProtocol.Response onMessage(DoesServerHaveIslandProtocol.Request message) {
        UUID islandUUID = UUID.fromString(message.islandUuid());
        return new DoesServerHaveIslandProtocol.Response(SkyBlockIsland.hasIsland(islandUUID), true, null);
    }
}
