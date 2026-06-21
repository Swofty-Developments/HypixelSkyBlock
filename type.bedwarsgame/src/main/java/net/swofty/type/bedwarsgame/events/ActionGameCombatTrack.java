package net.swofty.type.bedwarsgame.events;

import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.pvp.events.FinalDamageEvent;
import net.swofty.type.bedwarsgame.death.BedWarsCombatTracker;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionGameCombatTrack implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(FinalDamageEvent event) {
        if (!(event.getEntity() instanceof BedWarsPlayer victim)) {
            return;
        }

        BedWarsGame game = victim.getGame();
        if (game == null || game.getState() != GameState.IN_PROGRESS) {
            return;
        }

        if (event.getDamage().getAttacker() instanceof BedWarsPlayer attacker) {
            if (!victim.equals(attacker) && !isSameTeam(victim, attacker)) {
                BedWarsCombatTracker.recordAttack(victim, attacker);
            }
        }
    }

    private boolean isSameTeam(BedWarsPlayer first, BedWarsPlayer second) {
        BedWarsMapsConfig.TeamKey team1 = first.getTeamKey();
        BedWarsMapsConfig.TeamKey team2 = second.getTeamKey();
        if (team1 == null || team2 == null) {
            return false;
        }
        return team1.equals(team2);
    }
}

