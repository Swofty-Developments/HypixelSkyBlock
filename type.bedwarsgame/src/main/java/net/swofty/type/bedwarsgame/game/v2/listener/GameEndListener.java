package net.swofty.type.bedwarsgame.game.v2.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.ServerType;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.game.game.event.GameEndEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.tinylog.Logger;

public class GameEndListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameEnd(GameEndEvent event) {
        String gameId = event.gameId();
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(gameId);
        Logger.info("Ending BedWars game {}", gameId);

        game.getReplayManager().stopRecording();

        game.getGeneratorManager().stopAllGenerators();
        game.getGameEventManager().stop();

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            game.getPlayers().forEach(p -> p.sendTo(ServerType.BEDWARS_LOBBY));
            game.dispose();
        }).delay(TaskSchedule.seconds(10)).schedule();
    }

}
