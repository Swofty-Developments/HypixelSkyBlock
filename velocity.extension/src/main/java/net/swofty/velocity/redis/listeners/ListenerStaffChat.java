package net.swofty.velocity.redis.listeners;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;
import org.json.JSONObject;

import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.STAFF_CHAT)
public class ListenerStaffChat extends RedisListener {

    @Override
    public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
        // Broadcast the staff chat message to all servers
        broadcastToAllServers(message);
        return new JSONObject();
    }

    /**
     * Broadcasts a staff chat message to all registered game servers.
     * Used for both chat messages (from servers) and join/leave notifications (from proxy).
     *
     * @param message JSON containing "type" ("message", "join", or "leave") and relevant data
     */
    public static void broadcastToAllServers(JSONObject message) {
        GameManager.getServers().forEach((serverType, serverList) -> {
            serverList.forEach(gameServer -> {
                RedisMessage.sendMessageToServer(
                        gameServer.internalID(),
                        FromProxyChannels.BROADCAST_STAFF_CHAT,
                        message
                );
            });
        });
    }

    /**
     * Convenience method to broadcast a player join notification.
     *
     * @param playerUuid The UUID of the player who joined
     */
    public static void broadcastStaffJoin(UUID playerUuid) {
        JSONObject message = new JSONObject()
                .put("type", "join")
                .put("uuid", playerUuid.toString());
        broadcastToAllServers(message);
    }

    /**
     * Convenience method to broadcast a player leave notification.
     *
     * @param playerUuid The UUID of the player who left
     */
    public static void broadcastStaffLeave(UUID playerUuid) {
        JSONObject message = new JSONObject()
                .put("type", "leave")
                .put("uuid", playerUuid.toString());
        broadcastToAllServers(message);
    }
}
