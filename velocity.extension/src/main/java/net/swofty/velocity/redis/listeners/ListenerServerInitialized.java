package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.RegisterServerProtocol;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

import java.util.UUID;

@ChannelListener
public class ListenerServerInitialized extends RedisListener<
        RegisterServerProtocol.Request,
        RegisterServerProtocol.Response> {

    @Override
    public ProtocolObject<RegisterServerProtocol.Request, RegisterServerProtocol.Response> getProtocol() {
        return new RegisterServerProtocol();
    }

    @Override
    public RegisterServerProtocol.Response receivedMessage(RegisterServerProtocol.Request message, UUID serverUUID) {
        ServerType type = ServerType.valueOf(message.type());
        int port = message.port() != null ? message.port() : -1;

        String host = message.host() != null
                ? message.host()
                : ConfigProvider.settings().getHostName();

        int maxPlayers = message.maxPlayers();

        GameManager.GameServer server = GameManager.addServer(type, serverUUID, host, port, maxPlayers);
        return new RegisterServerProtocol.Response(
                server.registeredServer().getServerInfo().getAddress().getHostString(),
                server.registeredServer().getServerInfo().getAddress().getPort());
    }
}
