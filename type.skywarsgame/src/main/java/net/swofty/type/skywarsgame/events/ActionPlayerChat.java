package net.swofty.type.skywarsgame.events;

import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skywars.SkywarsLevelColor;
import net.swofty.type.skywarslobby.level.SkywarsLevelRegistry;
import net.swofty.type.generic.chat.StaffChat;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public class ActionPlayerChat implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerChatEvent event) {
        final SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();
        event.setCancelled(true);

        if (player.getDataHandler() == null) return;

        SkywarsDataHandler skywarsDataHandler = SkywarsDataHandler.getUser(player);
        if (skywarsDataHandler == null) {
            player.sendMessage("§cAn error occurred while processing your chat message. Please try again later.");
            return;
        }

        String message = event.getRawMessage();
        Rank rank = player.getRank();

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

        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
        if (game == null) return;

        boolean hideLevel = skywarsDataHandler.get(SkywarsDataHandler.Data.HIDE_LEVEL, DatapointBoolean.class).getValue();
        String levelPrefix;
        if (!hideLevel) {
            int level = SkywarsLevelRegistry.calculateLevel(
                    skywarsDataHandler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue()
            );
            levelPrefix = SkywarsLevelColor.getLevelDisplay(level) + " ";
        } else {
            levelPrefix = "";
        }

        for (SkywarsPlayer gamePlayer : game.getPlayers()) {
            if (rank.equals(Rank.DEFAULT))
                gamePlayer.sendMessage(levelPrefix + rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + "§7: " + finalMessage);
            else
                gamePlayer.sendMessage(levelPrefix + rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + "§f: " + finalMessage);
        }
    }
}
