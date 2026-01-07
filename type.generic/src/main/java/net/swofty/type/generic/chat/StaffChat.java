package net.swofty.type.generic.chat;

import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.type.generic.user.HypixelPlayer;
import org.json.JSONObject;

public final class StaffChat {
    private StaffChat() {}

    /**
     * Sends a staff chat message from a player.
     * The message is routed through the proxy to all servers.
     *
     * @param sender The player sending the message
     * @param message The message content
     */
    public static void sendMessage(HypixelPlayer sender, String message) {
        String formatted = "§b[STAFF] " + sender.getRank().getPrefix() + sender.getUsername() + "§f: " + message;
        broadcastViaProxy(formatted);
    }

    /**
     * Sends a staff notification (system message).
     * The message is routed through the proxy to all servers.
     *
     * @param message The notification message
     */
    public static void sendNotification(String message) {
        String formatted = "§b[STAFF] §7" + message;
        broadcastViaProxy(formatted);
    }

    private static void broadcastViaProxy(String formattedMessage) {
        JSONObject message = new JSONObject()
                .put("type", "message")
                .put("formatted_message", formattedMessage);

        ServerOutboundMessage.sendMessageToProxy(
                ToProxyChannels.STAFF_CHAT,
                message,
                response -> {
                    // Fire-and-forget, no response handling needed
                }
        );
    }
}

