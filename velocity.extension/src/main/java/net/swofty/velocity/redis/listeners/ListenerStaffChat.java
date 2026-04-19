package net.swofty.velocity.redis.listeners;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.BroadcastStaffChatProtocol;
import net.swofty.commons.protocol.objects.proxy.to.StaffChatProtocol;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;

import java.util.UUID;

@ChannelListener
public class ListenerStaffChat extends RedisListener<
        StaffChatProtocol.Request,
        StaffChatProtocol.Response> {

    @Override
    public ProtocolObject<StaffChatProtocol.Request, StaffChatProtocol.Response> getProtocol() {
        return new StaffChatProtocol();
    }

    @Override
    public StaffChatProtocol.Response receivedMessage(StaffChatProtocol.Request message, UUID serverUUID) {
        broadcastToAllServers(new BroadcastStaffChatProtocol.Request(
                message.type(), message.formattedMessage(), message.uuid()));
        return new StaffChatProtocol.Response();
    }

    public static void broadcastToAllServers(BroadcastStaffChatProtocol.Request message) {
        GameManager.getServers().forEach((serverType, serverList) -> {
            serverList.forEach(gameServer -> {
                RedisMessage.sendMessageToServer(
                        gameServer.internalID(),
                        new BroadcastStaffChatProtocol(),
                        message
                );
            });
        });
    }

    public static void broadcastStaffJoin(UUID playerUuid) {
        broadcastToAllServers(new BroadcastStaffChatProtocol.Request(
                "join", null, playerUuid.toString()));
    }

    public static void broadcastStaffLeave(UUID playerUuid) {
        broadcastToAllServers(new BroadcastStaffChatProtocol.Request(
                "leave", null, playerUuid.toString()));
    }
}
