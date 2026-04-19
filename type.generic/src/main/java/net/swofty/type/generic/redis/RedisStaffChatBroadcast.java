package net.swofty.type.generic.redis;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.proxy.from.BroadcastStaffChatProtocol;
import net.swofty.proxyapi.redis.TypedProxyHandler;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.commands.ChatCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;

public class RedisStaffChatBroadcast implements TypedProxyHandler<BroadcastStaffChatProtocol.Request, BroadcastStaffChatProtocol.Response> {
    @Override
    public ProtocolObject<BroadcastStaffChatProtocol.Request, BroadcastStaffChatProtocol.Response> getProtocol() {
        return new BroadcastStaffChatProtocol();
    }

    @Override
    public BroadcastStaffChatProtocol.Response onMessage(BroadcastStaffChatProtocol.Request message) {
        String type = message.type();

        switch (type) {
            case "message" -> {
                String formattedMessage = message.formattedMessage();
                sendToLocalStaff(formattedMessage, null);
            }
            case "join" -> {
                UUID playerUuid = UUID.fromString(message.uuid());
                handleJoinLeave(playerUuid, true);
            }
            case "leave" -> {
                UUID playerUuid = UUID.fromString(message.uuid());
                handleJoinLeave(playerUuid, false);
            }
        }

        return new BroadcastStaffChatProtocol.Response();
    }

    private void handleJoinLeave(UUID playerUuid, boolean isJoin) {
        try {
            HypixelDataHandler handler = HypixelDataHandler.getOfOfflinePlayer(playerUuid);
            Rank rank = handler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();

            if (!rank.isStaff()) {
                return;
            }

            String ign = handler.get(HypixelDataHandler.Data.IGN, DatapointString.class).getValue();
            String displayName = rank.getPrefix() + ign;
            String action = isJoin ? "joined" : "left";
            String formattedMessage = "§b[STAFF] §7" + displayName + " §7" + action + ".";

            sendToLocalStaff(formattedMessage, playerUuid);
        } catch (Exception e) {
        }
    }

    private void sendToLocalStaff(String message, UUID senderUuid) {
        HypixelGenericLoader.getLoadedPlayers().stream()
                .filter(player -> player.getRank().isStaff())
                .filter(player -> ChatCommand.isStaffViewEnabled(player.getUuid()) ||
                        (player.getUuid().equals(senderUuid)))
                .forEach(player -> player.sendMessage(message));
    }
}
