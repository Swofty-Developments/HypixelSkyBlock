package net.swofty.velocity.presence;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.objects.presence.UpdatePresenceProtocol;
import net.swofty.commons.redis.RedisClient;

import java.util.UUID;

public final class PresencePublisher {

    private PresencePublisher() {}

    public static void publish(Player player, boolean online, String serverType, UUID serverId) {
        PresenceInfo info = new PresenceInfo(
                player.getUniqueId(),
                online,
                serverType,
                serverId != null ? serverId.toString() : null,
                System.currentTimeMillis()
        );

        RedisClient.publishAllServices(
                new UpdatePresenceProtocol(),
                new UpdatePresenceProtocol.UpdatePresenceMessage(info)
        );
    }

    public static void publish(Player player, boolean online, RegisteredServer server, String serverType) {
        UUID serverId = null;
        if (server != null) {
            try {
                serverId = UUID.fromString(server.getServerInfo().getName());
            } catch (Exception ignored) {
            }
        }
        publish(player, online, serverType, serverId);
    }
}
