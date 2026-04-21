package net.swofty.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.game.v2.BedWarsTeam;
import net.swofty.type.bedwarsgame.replay.BedWarsReplayManager;
import net.swofty.type.game.game.event.TeamEliminatedEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class TeamEliminationListener implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(TeamEliminatedEvent<BedWarsTeam> event) {
        BedWarsTeam team = event.team();
        String teamColor = team.getColorCode();
        String teamName = team.getName();

        BedWarsGame game = (BedWarsGame) event.game();

        game.broadcastMessage(Component.text(""));
        game.broadcastMessage(Component.text("§f§lTEAM ELIMINATED > §c" + teamColor + teamName + " §chas been eliminated!"));
        game.broadcastMessage(Component.text(""));

        BedWarsReplayManager replayManager = game.getReplayManager();

        // Record to replay
        if (replayManager.isRecording()) {
            replayManager.recordTeamElimination(team.getTeamKey());
        }
    }
}
