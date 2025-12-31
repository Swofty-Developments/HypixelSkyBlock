package net.swofty.type.murdermysterygame.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerChatEvent;
import net.swofty.commons.StringUtility;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.role.GameRole;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.user.categories.Rank;

public class ActionPlayerChat implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerChatEvent event) {
        MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();
        event.setCancelled(true);

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;

        String message = event.getRawMessage();
        Rank rank = player.getRank();

        // Sanitize message
        if (!rank.isStaff()) {
            message = message.replaceAll("[^\\x00-\\x7F]", "");
        }

        String finalMessage = message;

        // Dead players can only talk to other dead players
        if (player.isEliminated() && game.getGameStatus() == GameStatus.IN_PROGRESS) {
            for (MurderMysteryPlayer gamePlayer : game.getPlayers()) {
                if (gamePlayer.isEliminated()) {
                    gamePlayer.sendMessage(Component.text("[DEAD] ", NamedTextColor.GRAY)
                            .append(Component.text(player.getUsername() + ": ", NamedTextColor.WHITE))
                            .append(Component.text(finalMessage, NamedTextColor.GRAY)));
                }
            }
            return;
        }

        // Normal chat
        for (MurderMysteryPlayer gamePlayer : game.getPlayers()) {
            gamePlayer.sendMessage(Component.text(rank.getPrefix() + StringUtility.getTextFromComponent(player.getName()) + ": ", NamedTextColor.WHITE)
                    .append(Component.text(finalMessage, NamedTextColor.WHITE)));
        }
    }
}
