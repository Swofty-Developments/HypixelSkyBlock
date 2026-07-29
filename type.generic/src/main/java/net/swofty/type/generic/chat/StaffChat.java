package net.swofty.type.generic.chat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.commons.protocol.objects.proxy.to.StaffChatProtocol;
import net.swofty.commons.redis.RedisClient;
import net.swofty.type.generic.user.HypixelPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaffChat {

    private static final StaffChatProtocol PROTOCOL = new StaffChatProtocol();

    public static void sendMessage(HypixelPlayer sender, String message) {
        String formatted = "§b[STAFF] " + sender.getRank().getPrefix() + sender.getUsername() + "§f: " + message;
        broadcastViaProxy(formatted);
    }

    public static void sendNotification(String message) {
        String formatted = "§b[STAFF] §7" + message;
        broadcastViaProxy(formatted);
    }

    private static void broadcastViaProxy(String formattedMessage) {
        RedisClient.requestProxy(PROTOCOL, new StaffChatProtocol.Request("message", formattedMessage, null));
    }
}
