package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.to.RequestServersProtocol;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.testflow.TestFlowManager;

import java.util.*;

@ChannelListener
public class ListenerServersInformation extends RedisListener<
        RequestServersProtocol.Request,
        RequestServersProtocol.Response> {

    @Override
    public ProtocolObject<RequestServersProtocol.Request, RequestServersProtocol.Response> getProtocol() {
        return new RequestServersProtocol();
    }

    @Override
    public RequestServersProtocol.Response receivedMessage(RequestServersProtocol.Request message, UUID serverUUID) {
        String requestType = message.requestType();
        boolean isInTestFlow = TestFlowManager.isServerInTestFlow(serverUUID);

        switch (requestType) {
            case "ALL" -> {
                List<GameManager.GameServer> servers = new ArrayList<>(GameManager.getServers().values().stream().flatMap(List::stream).toList());
                filterByTestFlow(servers, isInTestFlow);
                return new RequestServersProtocol.Response(UnderstandableProxyServer.toJSON(toUnderstandable(servers)).toString(), true, null);
            }
            case "TYPE" -> {
                List<GameManager.GameServer> servers = GameManager.getFromType(ServerType.valueOf(message.type()));
                if (servers == null) servers = new ArrayList<>();
                filterByTestFlow(servers, isInTestFlow);
                return new RequestServersProtocol.Response(UnderstandableProxyServer.toJSON(toUnderstandable(servers)).toString(), true, null);
            }
            case "UUID" -> {
                List<GameManager.GameServer> servers = new ArrayList<>(List.of(GameManager.getFromUUID(UUID.fromString(message.uuid()))));
                filterByTestFlow(servers, isInTestFlow);
                return new RequestServersProtocol.Response(UnderstandableProxyServer.toJSON(toUnderstandable(servers)).toString(), true, null);
            }
            case "PLAYER_UUID" -> {
                UUID uuidObject = UUID.fromString(message.uuid());
                Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(uuidObject);
                if (potentialPlayer.isEmpty()) {
                    return new RequestServersProtocol.Response(UnderstandableProxyServer.toJSON(List.of()).toString(), true, null);
                }
                Player player = potentialPlayer.get();
                List<GameManager.GameServer> servers = new ArrayList<>(Collections.singletonList(GameManager.getFromRegisteredServer(player.getCurrentServer().get().getServer())));
                filterByTestFlow(servers, isInTestFlow);
                return new RequestServersProtocol.Response(UnderstandableProxyServer.toJSON(toUnderstandable(servers)).toString(), true, null);
            }
        }

        throw new RuntimeException("Unknown request type: " + requestType);
    }

    private void filterByTestFlow(List<GameManager.GameServer> servers, boolean isInTestFlow) {
        if (isInTestFlow) {
            servers.removeIf(server -> {
                TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(server.internalID());
                return testFlowInstance == null || !testFlowInstance.hasServer(server.internalID());
            });
        } else {
            servers.removeIf(server -> {
                TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(server.internalID());
                return testFlowInstance != null;
            });
        }
    }

    private List<UnderstandableProxyServer> toUnderstandable(List<GameManager.GameServer> servers) {
        return servers.stream().map(server -> new UnderstandableProxyServer(
                server.displayName(),
                server.internalID(),
                GameManager.getTypeFromRegisteredServer(server.registeredServer()),
                server.registeredServer().getServerInfo().getAddress().getPort(),
                server.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
                server.maxPlayers(),
                server.shortDisplayName()
        )).toList();
    }
}
