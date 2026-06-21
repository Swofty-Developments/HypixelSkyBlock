package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerDisconnectGameEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class PlayerGameDisconnectListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerDisconnect(PlayerDisconnectGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        BedWarsGame game = player.getGame();
        if (game != null) {
            String teamColor = game.getPlayerTeam(player.getUuid())
                .map(BedWarsTeam::getColorCode)
                .orElse("§7");

            game.broadcastMessage(Component.text(teamColor + player.getUsername() + " §7disconnected."));
        }
    }

}
