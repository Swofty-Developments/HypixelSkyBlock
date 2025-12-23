package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;
import org.json.JSONObject;

import java.util.*;

@ChannelListener(channel = ToProxyChannels.REQUEST_SERVERS)
public class ListenerServersInformation extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        String requestType = message.getString("request_type");
        boolean isInTestFlow = TestFlowManager.isServerInTestFlow(serverUUID);

        switch (requestType) {
            case "ALL" -> {
                List<GameManager.GameServer> servers = new ArrayList<>(GameManager.getServers().values().stream().flatMap(List::stream).toList());

                if (isInTestFlow) {
                    // Remove all servers that are not in the current test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return !testFlowInstance.hasServer(server.internalID());
                    });
                } else {
                    // Remove all servers that are in a test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return testFlowInstance != null;
                    });
                }

                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
                        server.maxPlayers(),
                        server.shortDisplayName()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
            case "TYPE" -> {
                String type = message.getString("type");
                List<GameManager.GameServer> servers = GameManager.getFromType(ServerType.valueOf(type));
                if (servers == null) servers = new ArrayList<>();

                if (isInTestFlow) {
                    // Remove all servers that are not in the current test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return testFlowInstance == null || !testFlowInstance.hasServer(server.internalID());
                    });
                } else {
                    // Remove all servers that are in a test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return testFlowInstance != null;
                    });
                }

                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
                        server.maxPlayers(),
                        server.shortDisplayName()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
            case "UUID" -> {
                String uuid = message.getString("uuid");
                UUID uuidObject = UUID.fromString(uuid);
                List<GameManager.GameServer> servers = new ArrayList<>(List.of(GameManager.getFromUUID(uuidObject)));

                if (isInTestFlow) {
                    // Remove all servers that are not in the current test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return !testFlowInstance.hasServer(server.internalID());
                    });
                } else {
                    // Remove all servers that are in a test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return testFlowInstance != null;
                    });
                }

                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
                        server.maxPlayers(),
                        server.shortDisplayName()
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
                List<GameManager.GameServer> servers = new ArrayList<>(Collections.singletonList(GameManager.getFromRegisteredServer(player.getCurrentServer().get().getServer())));

                if (isInTestFlow) {
                    // Remove all servers that are not in the current test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return !testFlowInstance.hasServer(server.internalID());
                    });
                } else {
                    // Remove all servers that are in a test flow
                    servers.removeIf(server -> {
                        TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
                                server.internalID()
                        );

                        return testFlowInstance != null;
                    });
                }

                List<UnderstandableProxyServer> understandableProxyServers = servers.stream().map(server -> new UnderstandableProxyServer(
                        server.displayName(),
                        server.internalID(),
                        GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
                        server.maxPlayers(),
                        server.shortDisplayName()
                )).toList();
                return new JSONObject().put("servers_list", UnderstandableProxyServer.toJSON(understandableProxyServers));
            }
        }

        throw new RuntimeException("Unknown request type: " + requestType);
    }
}
