package net.swofty.type.bedwarsgame.game.v2.listener;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.game.game.event.GameStartEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import org.tinylog.Logger;

import java.util.Map;

public class GameStartListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameStart(GameStartEvent event) {
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(event.getGameId());
        Logger.info("Starting BedWars game {}", event.gameId());

        // Prepare world
        game.getWorldManager().clearExistingBeds();

        // Assign players to teams
        game.autoAssignTeams();

        // Get active teams and set up their areas
        Map<BedWarsMapsConfig.TeamKey, BedWarsMapsConfig.MapTeam> activeTeamConfigs = game.getActiveTeamConfigs();

        game.getWorldManager().placeBeds(activeTeamConfigs);
        game.getWorldManager().spawnShopNPCs(game.getMapEntry().getConfiguration().getTeams());

        // Start generators
        game.getGeneratorManager().startTeamGenerators(activeTeamConfigs);
        game.getGeneratorManager().startGlobalGenerators();

        // Start game event progression
        game.getGameEventManager().start();

        // Teleport players to their spawn points
        game.teleportPlayersToSpawns();

        // Start time-played XP task
        game.startTimePlayedRewards();

        // Start replay recording
        game.getReplayManager().startRecording();

        // Send game start message
        game.sendGameStartMessage();

        // just correct in case
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (game.getState() != GameState.IN_PROGRESS) {
                return TaskSchedule.stop();
            }
            for (BedWarsPlayer player : game.getPlayers()) {
                player.updateBelowTag();
            }
            return TaskSchedule.seconds(10);
        }, TaskSchedule.seconds(1));

        Logger.info("BedWars game {} started with {} active teams", event.gameId(), activeTeamConfigs.size());
    }

}
