package net.swofty.velocity.redis.listeners;

import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.commons.proxy.requirements.to.PlayerCountRequirements;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.PLAYER_COUNT)
public class ListenerPlayerCount extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        PlayerCountRequirements.LookupType lookupType =
                PlayerCountRequirements.LookupType.valueOf(message.getString("lookup-type"));
        String lookupValue = message.getString("lookup-value");

        switch (lookupType) {
            case ALL -> {
                return new JSONObject().put("player-count", GameManager.getServers().values().stream()
                        .flatMap(List::stream)
                        .mapToInt(server -> server.registeredServer().getPlayersConnected().size())
                        .sum());
            }
            case TYPE -> {
                ServerType serverType = ServerType.valueOf(lookupValue);
                return new JSONObject().put("player-count", GameManager.getServers().entrySet().stream()
                        .filter(entry -> entry.getKey() == serverType)
                        .flatMap(entry -> entry.getValue().stream())
                        .mapToInt(server -> server.registeredServer().getPlayersConnected().size())
                        .sum());
            }
            case UUID -> {
                UUID uuid = UUID.fromString(lookupValue);
                return new JSONObject().put("player-count", GameManager.getFromUUID(uuid).registeredServer().getPlayersConnected().size());
            }
        }
        throw new RuntimeException("Unknown lookup type: " + lookupType);
    }
}
