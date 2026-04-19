package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.PlayerCountProtocol;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

import java.util.List;
import java.util.UUID;

@ChannelListener
public class ListenerPlayerCount extends RedisListener<
        PlayerCountProtocol.Request,
        PlayerCountProtocol.Response> {

    @Override
    public ProtocolObject<PlayerCountProtocol.Request, PlayerCountProtocol.Response> getProtocol() {
        return new PlayerCountProtocol();
    }

    @Override
    public PlayerCountProtocol.Response receivedMessage(PlayerCountProtocol.Request message, UUID serverUUID) {
        switch (message.lookupType()) {
            case ALL -> {
                int count = GameManager.getServers().values().stream()
                        .flatMap(List::stream)
                        .mapToInt(server -> server.registeredServer().getPlayersConnected().size())
                        .sum();
                return new PlayerCountProtocol.Response(count);
            }
            case TYPE -> {
                ServerType serverType = ServerType.valueOf(message.lookupValue());
                int count = GameManager.getServers().entrySet().stream()
                        .filter(entry -> entry.getKey() == serverType)
                        .flatMap(entry -> entry.getValue().stream())
                        .mapToInt(server -> server.registeredServer().getPlayersConnected().size())
                        .sum();
                return new PlayerCountProtocol.Response(count);
            }
            case UUID -> {
                UUID uuid = UUID.fromString(message.lookupValue());
                int count = GameManager.getFromUUID(uuid).registeredServer().getPlayersConnected().size();
                return new PlayerCountProtocol.Response(count);
            }
        }
        throw new RuntimeException("Unknown lookup type: " + message.lookupType());
    }
}
