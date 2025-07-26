package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import org.json.JSONObject;

import javax.swing.text.html.Option;
import java.util.*;

@ChannelListener(channel = ToProxyChannels.REQUEST_SERVERS)
public class ListenerServersInformation extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        String requestType = message.getString("request_type");
        switch (requestType) {
            case "ALL" -> {
                List<GameManager.GameServer> servers = GameManager.getServers().values().stream().flatMap(List::stream).toList();
                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
            case "TYPE" -> {
                String type = message.getString("type");
                List<GameManager.GameServer> servers = GameManager.getFromType(ServerType.valueOf(type));
                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
            case "UUID" -> {
                String uuid = message.getString("uuid");
                UUID uuidObject = UUID.fromString(uuid);
                List<GameManager.GameServer> servers = List.of(GameManager.getFromUUID(uuidObject));
                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
            case "PLAYER_UUID" -> {
                String uuid = message.getString("uuid");
                UUID uuidObject = UUID.fromString(uuid);
                Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(uuidObject);
                if (potentialPlayer.isEmpty()) {
                    return new JSONObject();
                }
                Player player = potentialPlayer.get();
                List<GameManager.GameServer> servers = Collections.singletonList(GameManager.getFromRegisteredServer(player.getCurrentServer().get().getServer()));
                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
        }

        throw new RuntimeException("Unknown request type: " + requestType);
    }
}
