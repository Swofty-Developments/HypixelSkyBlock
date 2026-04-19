package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.GivePlayersOriginTypeProtocol;
import net.swofty.commons.protocol.objects.proxy.from.RefreshCoopDataProtocol;
import net.swofty.commons.protocol.objects.proxy.from.RunEventProtocol;
import net.swofty.commons.protocol.objects.proxy.from.TeleportProtocol;
import net.swofty.commons.protocol.objects.proxy.to.PlayerHandlerProtocol;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;
import net.swofty.velocity.presence.PresencePublisher;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ChannelListener
public class ListenerPlayerHandler extends RedisListener<
        PlayerHandlerProtocol.Request,
        PlayerHandlerProtocol.Response> {

    private static final PlayerHandlerProtocol.Response EMPTY = new PlayerHandlerProtocol.Response(Map.of());

    @Override
    public ProtocolObject<PlayerHandlerProtocol.Request, PlayerHandlerProtocol.Response> getProtocol() {
        return new PlayerHandlerProtocol();
    }

    @Override
    public PlayerHandlerProtocol.Response receivedMessage(PlayerHandlerProtocol.Request message, UUID serverUUID) {
        UUID uuid = UUID.fromString(message.uuid());
        PlayerHandlerProtocol.Action action = message.action();
        Map<String, Object> data = message.data() != null ? message.data() : Map.of();

        Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(uuid);
        if (potentialPlayer.isEmpty()) {
            if (action == PlayerHandlerProtocol.Action.IS_ONLINE) {
                return new PlayerHandlerProtocol.Response(Map.of("isOnline", false));
            }
            return EMPTY;
        }
        if (action == PlayerHandlerProtocol.Action.IS_ONLINE) {
            Player player = potentialPlayer.get();
            publishPresence(player, true);
            return new PlayerHandlerProtocol.Response(Map.of("isOnline", true));
        }
        Player player = potentialPlayer.get();
        Optional<ServerConnection> potentialServer = player.getCurrentServer();

        switch (action) {
            case GET_SERVER -> {
                UUID playersServer = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                GameManager.GameServer serverInfo = GameManager.getFromUUID(playersServer);
                if (serverInfo == null) {
                    return EMPTY;
                }

                publishPresence(player, true);
                UnderstandableProxyServer ups = new UnderstandableProxyServer(
                    serverInfo.displayName(),
                    serverInfo.internalID(),
                    GameManager.getTypeFromRegisteredServer(serverInfo.registeredServer()),
                    serverInfo.registeredServer().getServerInfo().getAddress().getPort(),
                    serverInfo.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
                    serverInfo.maxPlayers(),
                    serverInfo.shortDisplayName()
                );
                return new PlayerHandlerProtocol.Response(Map.of("server", ups.toJSON().toMap()));
            }
            case TRANSFER_WITH_UUID -> {
                UUID server = UUID.fromString((String) data.get("server_uuid"));
                GameManager.GameServer serverInfo = GameManager.getFromUUID(server);
                TransferHandler transferHandler = new TransferHandler(player);

                if (serverInfo == null) {
                    player.sendMessage(Component.text(
                        "§cWe encountered an issue while attempting to locate the server on the network. Please try again later."
                    ));
                    return EMPTY;
                }

                player.sendMessage(Component.text("§7Sending to server " + serverInfo.displayName() + "..."));

                if (!serverInfo.hasEmptySlots()) {
                    player.sendMessage(Component.text(
                        "§cAttempted to connect to " + serverInfo.displayName() + ", but there are no empty slots available. Please try again later."
                    ));
                    return EMPTY;
                }

                transferHandler.addToDisregard();
                transferHandler.transferTo(serverInfo.registeredServer())
                    .thenRun(transferHandler::removeFromDisregard);
            }
            case TRANSFER -> {
                ServerType type = ServerType.valueOf((String) data.get("type"));
                if (!GameManager.hasType(type)
                    || new TransferHandler(player).isInLimbo()
                    || !GameManager.isAnyEmpty(type)) {
                    player.sendMessage(Component.text(
                        "§cAttempted to transfer to a " + StringUtility.toNormalCase(type.name()) + " server, but there are no empty slots available. Please try again later."
                    ));
                    return EMPTY;
                }
                new TransferHandler(player).transferTo(type);
            }
            case LIMBO -> new TransferHandler(player).sendToLimbo().join();
            case TELEPORT -> {
                if (potentialServer.isEmpty()) {
                    return EMPTY;
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                Number x = (Number) data.get("x");
                Number y = (Number) data.get("y");
                Number z = (Number) data.get("z");
                Number yaw = (Number) data.get("yaw");
                Number pitch = (Number) data.get("pitch");
                RedisMessage.sendMessageToServer(server,
                    new TeleportProtocol(),
                    new TeleportProtocol.Request(uuid.toString(),
                        x.doubleValue(), y.doubleValue(), z.doubleValue(),
                        yaw.floatValue(), pitch.floatValue())).join();
            }
            case EVENT -> {
                if (potentialServer.isEmpty()) {
                    return EMPTY;
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                RedisMessage.sendMessageToServer(server,
                    new RunEventProtocol(),
                    new RunEventProtocol.Request(uuid.toString(),
                        (String) data.get("event"),
                        (String) data.get("data"))).join();
            }
            case REFRESH_COOP_DATA -> {
                if (potentialServer.isEmpty()) {
                    return EMPTY;
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                RedisMessage.sendMessageToServer(server,
                    new RefreshCoopDataProtocol(),
                    new RefreshCoopDataProtocol.Request(uuid.toString(),
                        (String) data.get("datapoint"))).join();
            }
            case MESSAGE -> {
                String messageToSend = (String) data.get("message");
                player.sendMessage(JSONComponentSerializer.json().deserialize(messageToSend));
            }
        }
        return EMPTY;
    }

    private void publishPresence(Player player, boolean online) {
        try {
            Optional<ServerConnection> serverConn = player.getCurrentServer();
            var type = serverConn.map(conn -> GameManager.getTypeFromRegisteredServer(conn.getServer())).orElse(null);
            PresencePublisher.publish(player, online, serverConn.map(ServerConnection::getServer).orElse(null),
                type != null ? type.name() : null);
        } catch (Exception ignored) {
        }
    }
}
