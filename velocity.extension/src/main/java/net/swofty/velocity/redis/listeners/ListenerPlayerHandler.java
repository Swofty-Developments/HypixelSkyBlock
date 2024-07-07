package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.viaversion.viaversion.api.Via;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.commons.proxy.requirements.to.PlayerHandlerRequirements;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.BalanceConfiguration;
import net.swofty.velocity.gamemanager.BalanceConfigurations;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.PLAYER_HANDLER)
public class ListenerPlayerHandler extends RedisListener {
    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        UUID uuid = UUID.fromString(message.getString("uuid"));
        PlayerHandlerRequirements.PlayerHandlerActions action =
                PlayerHandlerRequirements.PlayerHandlerActions.valueOf(
                        message.getString("action"));

        Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(uuid);
        if (potentialPlayer.isEmpty()) {
            if (action == PlayerHandlerRequirements.PlayerHandlerActions.IS_ONLINE) {
                return new JSONObject().put("isOnline", false);
            }
            return new JSONObject();
        }
        if (action == PlayerHandlerRequirements.PlayerHandlerActions.IS_ONLINE) {
            return new JSONObject().put("isOnline", true);
        }
        Player player = potentialPlayer.get();
        Optional<ServerConnection> potentialServer = player.getCurrentServer();

        switch (action) {
            case TRANSFER -> {
                ServerType type = ServerType.valueOf(message.getString("type"));
                if (!GameManager.hasType(type) || TransferHandler.playersInLimbo.contains(player)) {
                    return new JSONObject();
                }
                GameManager.GameServer toSendTo = BalanceConfigurations.getServerFor(player, type);
                new TransferHandler(player).standardTransferTo(player.getCurrentServer().get().getServer(), toSendTo.registeredServer());
            }
            case TELEPORT -> {
                if (potentialServer.isEmpty()) {
                    return new JSONObject();
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                return RedisMessage.sendMessageToServer(server,
                        FromProxyChannels.TELEPORT,
                        message).join();
            }
            case BANK_HASH -> {
                if (potentialServer.isEmpty()) {
                    return new JSONObject();
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                return RedisMessage.sendMessageToServer(server,
                        FromProxyChannels.GET_BANK_HASH,
                        message).join();
            }
            case VERSION -> {
                return new JSONObject().put("version", Via.getAPI().getPlayerVersion(player.getUniqueId()));
            }
            case EVENT -> {
                if (potentialServer.isEmpty()) {
                    return new JSONObject();
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                RedisMessage.sendMessageToServer(server,
                        FromProxyChannels.RUN_EVENT_ON_SERVER,
                        message
                ).join();
            }
            case REFRESH_COOP_DATA -> {
                if (potentialServer.isEmpty()) {
                    return new JSONObject();
                }
                UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
                RedisMessage.sendMessageToServer(server,
                        FromProxyChannels.REFRESH_COOP_DATA_ON_SERVER,
                        message
                ).join();
            }
            case MESSAGE -> {
                String messageToSend = message.getString("message");
                player.sendMessage(JSONComponentSerializer.json().deserialize(messageToSend));
            }
        };
        return new JSONObject();
    }
}
