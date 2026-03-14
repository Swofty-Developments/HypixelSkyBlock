package net.swofty.type.skywarsgame.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skywarsgame.SkywarsGameScoreboard;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public class ActionPlayerDisconnect implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onDisconnect(PlayerDisconnectEvent event) {
        SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();
        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);

        if (game != null) {
            game.disconnect(player);
        }
        // git is wierd with this file idk
        SkywarsGameScoreboard.removeCache(player);
    }
}
