package net.swofty.velocity.redis.listeners;

import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.objects.proxy.to.ProxyIsOnlineProtocol;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.commons.redis.RedisMessageContext;
import net.swofty.commons.redis.RedisMessageHandler;

import java.util.UUID;

public class ListenerProxyOnline implements RedisMessageHandler<
        ProxyIsOnlineProtocol.Request,
        ProxyIsOnlineProtocol.Response> {

    @Override
    public RedisProtocol<ProxyIsOnlineProtocol.Request, ProxyIsOnlineProtocol.Response> protocol() {
        return new ProxyIsOnlineProtocol();
    }

    @Override
    public ProxyIsOnlineProtocol.Response handle(ProxyIsOnlineProtocol.Request message, RedisMessageContext context) {
        if (GameManager.getFromUUID(UUID.fromString(context.origin().id())) == null) {
            return new ProxyIsOnlineProtocol.Response(false, true, null);
        }
        return new ProxyIsOnlineProtocol.Response(true, true, null);
    }
}
