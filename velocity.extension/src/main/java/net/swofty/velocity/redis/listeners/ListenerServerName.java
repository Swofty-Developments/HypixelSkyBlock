package net.swofty.velocity.redis.listeners;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.RequestServerNameProtocol;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

import java.util.UUID;

@ChannelListener
public class ListenerServerName extends RedisListener<
        RequestServerNameProtocol.Request,
        RequestServerNameProtocol.Response> {

    @Override
    public ProtocolObject<RequestServerNameProtocol.Request, RequestServerNameProtocol.Response> getProtocol() {
        return new RequestServerNameProtocol();
    }

    @Override
    public RequestServerNameProtocol.Response receivedMessage(RequestServerNameProtocol.Request message, UUID serverUUID) {
        GameManager.GameServer server = GameManager.getFromUUID(serverUUID);
        return new RequestServerNameProtocol.Response(server.displayName(), server.shortDisplayName(), true, null);
    }
}
