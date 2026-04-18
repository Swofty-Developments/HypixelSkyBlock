package net.swofty.type.bedwarsgame.game.v2.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.game.game.event.GameDisposeEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class GameDisposalListener implements HypixelEventClass {
    private static final int MAX_UNREGISTER_ATTEMPTS = 120;

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameDispose(GameDisposeEvent event) {
        BedWarsGame disposedGame = TypeBedWarsGameLoader.getGameById(event.gameId());
        TypeBedWarsGameLoader.getGames().removeIf(game -> game.getGameId().equals(event.gameId()));

        if (disposedGame != null) {
            unregisterInstanceWhenEmpty(disposedGame, 0);
        }
    }

    private void unregisterInstanceWhenEmpty(BedWarsGame game, int attempts) {
        if (game.getInstance().getPlayers().isEmpty() || attempts >= MAX_UNREGISTER_ATTEMPTS) {
            MinecraftServer.getInstanceManager().unregisterInstance(game.getInstance());
            return;
        }

        MinecraftServer.getSchedulerManager().buildTask(() -> unregisterInstanceWhenEmpty(game, attempts + 1))
            .delay(TaskSchedule.millis(500))
            .schedule();
    }

}
