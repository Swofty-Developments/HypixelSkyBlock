package net.swofty.type.generic.presence;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServiceType;
import net.swofty.commons.presence.PresenceInfo;
import net.swofty.commons.protocol.objects.presence.UpdatePresenceProtocolObject;
import net.swofty.proxyapi.redis.ServerOutboundMessage;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.user.HypixelPlayer;

/**
 * Periodically refreshes player presence to the friend service to avoid stale status.
 */
public final class PresenceHeartbeat {
    private PresenceHeartbeat() {}

    public static void start() {
        MinecraftServer.getSchedulerManager().buildTask(PresenceHeartbeat::pulse)
                .delay(TaskSchedule.seconds(2))
                .repeat(TaskSchedule.seconds(10))
                .schedule();
    }

    private static void pulse() {
        String serverType = null;
        String serverId = null;
        try {
            serverType = HypixelConst.getTypeLoader().getType().name();
            if (HypixelConst.getServerUUID() != null) {
                serverId = HypixelConst.getServerUUID().toString();
            } else if (HypixelConst.getServerName() != null) {
                serverId = HypixelConst.getServerName();
            }
        } catch (Exception ignored) {
        }

        for (HypixelPlayer player : HypixelGenericLoader.getLoadedPlayers()) {
            PresenceInfo info = new PresenceInfo(
                    player.getUuid(),
                    true,
                    serverType,
                    serverId,
                    System.currentTimeMillis()
            );

            ServerOutboundMessage.sendMessageToService(
                    ServiceType.FRIEND,
                    new UpdatePresenceProtocolObject(),
                    new UpdatePresenceProtocolObject.UpdatePresenceMessage(info),
                    (ignored) -> {}
            );
        }
    }
}

