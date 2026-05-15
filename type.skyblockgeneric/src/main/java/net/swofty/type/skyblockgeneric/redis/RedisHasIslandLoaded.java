package net.swofty.type.skyblockgeneric.redis;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.from.DoesServerHaveIslandProtocol;
import net.swofty.commons.redis.RedisMessageHandler;
import net.swofty.type.skyblockgeneric.user.island.SkyBlockIsland;

import java.util.UUID;
import net.swofty.commons.redis.RedisMessageContext;

public class RedisHasIslandLoaded implements RedisMessageHandler<DoesServerHaveIslandProtocol.Request, DoesServerHaveIslandProtocol.Response> {
    @Override
    public RedisProtocol<DoesServerHaveIslandProtocol.Request, DoesServerHaveIslandProtocol.Response> protocol() {
        return new DoesServerHaveIslandProtocol();
    }

    @Override
    public DoesServerHaveIslandProtocol.Response handle(DoesServerHaveIslandProtocol.Request message, RedisMessageContext context) {
        UUID islandUUID = UUID.fromString(message.islandUuid());
        return new DoesServerHaveIslandProtocol.Response(SkyBlockIsland.hasIsland(islandUUID), true, null);
    }
}
