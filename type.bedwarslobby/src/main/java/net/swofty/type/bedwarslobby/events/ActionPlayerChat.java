package net.swofty.type.bedwarslobby.events;

import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.StringUtility;
import net.swofty.commons.bedwars.BedwarsLevelColor;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.chat.StaffChat;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;

public class ActionPlayerChat implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = false)
    public void run(PlayerChatEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        event.setCancelled(true);

        HypixelDataHandler dataHandler = player.getDataHandler();
        if (dataHandler == null) return;

        BedWarsDataHandler bedWarsDataHandler = BedWarsDataHandler.getUser(player);
        if (bedWarsDataHandler == null) {
            player.sendMessage("§cAn error occurred while processing your chat message. Please try again later.");
            return;
        }

        String message = event.getRawMessage();
        Rank rank = player.getRank();

        // Sanitize message to remove any special Unicode characters
        if (!rank.isStaff())
            message = message.replaceAll("[^\\x00-\\x7F]", "");

        String finalMessage = message;

        DatapointChatType.Chats chatType = player.getChatType().currentChatType;
        if (chatType == DatapointChatType.Chats.STAFF) {
            if (!rank.isStaff()) {
                player.sendMessage("§cUnknown chat type.");
                player.getChatType().switchTo(DatapointChatType.Chats.ALL);
                return;
            }
            StaffChat.sendMessage(player, finalMessage);
            return;
        }

        if (chatType == DatapointChatType.Chats.PARTY) {
            if (!PartyManager.isInParty(player)) {
                player.sendMessage("§cYou are not in a party and were moved to the ALL channel.");
                player.getChatType().switchTo(DatapointChatType.Chats.ALL);
                return;
            }

            PartyManager.sendChat(player, message);
            return;
        }

        List<HypixelPlayer> receivers = HypixelGenericLoader.getLoadedPlayers();

        String levelPrefix = BedwarsLevelColor.constructLevelBrackets(
                BedwarsLevelUtil.calculateLevel(bedWarsDataHandler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class).getValue())
        ) + " ";

        receivers.forEach(onlinePlayer -> {
            if (rank.equals(Rank.DEFAULT))
                onlinePlayer.sendMessage(levelPrefix + rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + "§7: " + finalMessage);
            else
                onlinePlayer.sendMessage(levelPrefix + rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + "§f: " + finalMessage);
        });
    }
}

