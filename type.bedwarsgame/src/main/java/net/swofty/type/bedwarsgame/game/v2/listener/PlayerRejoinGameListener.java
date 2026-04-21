package net.swofty.type.bedwarsgame.game.v2.listener;

import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.event.PlayerRejoinGameEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class PlayerRejoinGameListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerGameRejoin(PlayerRejoinGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        BedWarsGame game = player.getGame();

        game.onPlayerRejoin(player, event.disconnectedPlayerData());
    }

}
