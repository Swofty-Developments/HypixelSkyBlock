package net.swofty.velocity.redis.listeners;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.ProxyIsOnlineProtocol;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

import java.util.UUID;

@ChannelListener
public class ListenerProxyOnline extends RedisListener<
        ProxyIsOnlineProtocol.Request,
        ProxyIsOnlineProtocol.Response> {

    @Override
    public ProtocolObject<ProxyIsOnlineProtocol.Request, ProxyIsOnlineProtocol.Response> getProtocol() {
        return new ProxyIsOnlineProtocol();
    }

    @Override
    public ProxyIsOnlineProtocol.Response receivedMessage(ProxyIsOnlineProtocol.Request message, UUID serverUUID) {
        if (GameManager.getFromUUID(serverUUID) == null) {
            return new ProxyIsOnlineProtocol.Response(false, true, null);
        }
        return new ProxyIsOnlineProtocol.Response(true, true, null);
    }
}
