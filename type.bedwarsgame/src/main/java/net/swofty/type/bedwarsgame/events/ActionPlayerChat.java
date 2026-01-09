package net.swofty.type.bedwarsgame.events;

import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.BedwarsLevelColor;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.chat.StaffChat;
import net.swofty.type.generic.data.datapoints.DatapointChatType;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.party.PartyManager;
import net.swofty.type.generic.user.categories.Rank;

import java.util.List;

public class ActionPlayerChat implements HypixelEventClass {

	@HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
	public void run(PlayerChatEvent event) {
		final BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		event.setCancelled(true);

		Game game = player.getGame();
		if (game == null) return;

		BedWarsDataHandler bedWarsDataHandler = BedWarsDataHandler.getUser(player);
		if (bedWarsDataHandler == null) {
			player.sendMessage("§cAn error occurred while processing your chat message. Please try again later.");
			return;
		}

		String message = event.getRawMessage();
		Rank rank = player.getRank();

		// Sanitize a message to remove any special Unicode characters
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

		if (game.getGameStatus() == GameStatus.WAITING) {
			String textColor = rank.equals(Rank.DEFAULT) ? "§7" : "§f";

			game.getPlayers().forEach(onlinePlayer -> {
				onlinePlayer.sendMessage(rank.getPrefix() + player.getUsername() + textColor + ": " + finalMessage);
			});
			return;
		}

		List<BedWarsPlayer> receivers;
		if (game.getBedwarsGameType() == BedwarsGameType.SOLO) {
			receivers = game.getPlayers();
		} else {
			receivers = game.getTeamManager().getPlayersOnTeam(player.getTeamKey()).stream().map(p -> (BedWarsPlayer) p).toList();
		}

		String levelPrefix = BedwarsLevelColor.constructLevelBrackets(
				BedwarsLevelUtil.calculateLevel(bedWarsDataHandler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class).getValue())
		) + " ";
		String textColor = rank.equals(Rank.DEFAULT) ? "§7" : "§f";

		receivers.forEach(onlinePlayer -> {
			onlinePlayer.sendMessage(levelPrefix + rank.getPrefix() + player.getUsername() + textColor + ": " + finalMessage);
		});
	}
}

