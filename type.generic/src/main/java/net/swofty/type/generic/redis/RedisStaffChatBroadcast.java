package net.swofty.type.generic.redis;

import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.proxyapi.redis.ProxyToClient;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.command.commands.ChatCommand;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import org.json.JSONObject;

import java.util.UUID;

public class RedisStaffChatBroadcast implements ProxyToClient {
    @Override
    public FromProxyChannels getChannel() {
        return FromProxyChannels.BROADCAST_STAFF_CHAT;
    }

    @Override
    public JSONObject onMessage(JSONObject message) {
        String type = message.getString("type");

        switch (type) {
            case "message" -> {
                // Staff chat message - already formatted, just send to local staff
                String formattedMessage = message.getString("formatted_message");
                sendToLocalStaff(formattedMessage, null);
            }
            case "join" -> {
                UUID playerUuid = UUID.fromString(message.getString("uuid"));
                handleJoinLeave(playerUuid, true);
            }
            case "leave" -> {
                UUID playerUuid = UUID.fromString(message.getString("uuid"));
                handleJoinLeave(playerUuid, false);
            }
        }

        return new JSONObject();
    }

    private void handleJoinLeave(UUID playerUuid, boolean isJoin) {
        try {
            // Get player data (will check cache first, then query DB)
            HypixelDataHandler handler = HypixelDataHandler.getOfOfflinePlayer(playerUuid);
            Rank rank = handler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();

            // Only send notification if the player is staff
            if (!rank.isStaff()) {
                return;
            }

            String ign = handler.get(HypixelDataHandler.Data.IGN, DatapointString.class).getValue();
            String displayName = rank.getPrefix() + ign;
            String action = isJoin ? "joined" : "left";
            String formattedMessage = "§b[STAFF] §7" + displayName + " §7" + action + ".";

            sendToLocalStaff(formattedMessage, playerUuid);
        } catch (Exception e) {
            // Player may not have data yet (first join), silently ignore
        }
    }

    private void sendToLocalStaff(String message, UUID senderUuid) {
        HypixelGenericLoader.getLoadedPlayers().stream()
                .filter(player -> player.getRank().isStaff())
                .filter(player -> ChatCommand.isStaffViewEnabled(player.getUuid()) ||
                        (senderUuid != null && player.getUuid().equals(senderUuid)))
                .forEach(player -> player.sendMessage(message));
    }
}
