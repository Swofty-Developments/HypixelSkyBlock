package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;

import java.util.List;
import java.util.UUID;

@ChannelListener(channel = "player-count")
public class ListenerPlayerCount extends RedisListener {
    @Override
    public String receivedMessage(String message, UUID serverUUID) {
        if (message.equalsIgnoreCase("ALL")) {
            return String.valueOf(GameManager.getServers().values().stream()
                    .flatMap(List::stream)
                    .mapToInt(server -> server.server().getPlayersConnected().size())
                    .sum());
        } else {
            ServerType serverType = ServerType.valueOf(message);
            int count = GameManager.getServers().entrySet().stream()
                    .filter(entry -> entry.getKey() == serverType)
                    .flatMap(entry -> entry.getValue().stream())
                    .mapToInt(server -> server.server().getPlayersConnected().size())
                    .sum();
            return String.valueOf(count);
        }
    }
}
