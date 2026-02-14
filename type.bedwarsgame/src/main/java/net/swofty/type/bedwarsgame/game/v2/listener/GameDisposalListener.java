package net.swofty.type.bedwarsgame.game.v2.listener;

import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.game.game.event.GameDisposeEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class GameDisposalListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameDispose(GameDisposeEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.gameId());
        TypeBedWarsGameLoader.getGames().remove(game);
    }

}
