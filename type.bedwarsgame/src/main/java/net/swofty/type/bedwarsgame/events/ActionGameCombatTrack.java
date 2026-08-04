package net.swofty.type.bedwarsgame.events;

import io.github.term4.polyp.api.event.damage.DamageAppliedEvent;
import io.github.term4.polyp.mechanics.projectile.entities.ProjectileEntity;
import net.minestom.server.entity.Entity;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.death.BedWarsCombatTracker;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.game.game.GameState;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.PhasedEvent;

public class ActionGameCombatTrack implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = false)
    public void run(DamageAppliedEvent event) {
        if (!(event.target() instanceof BedWarsPlayer victim)) {
            return;
        }

        BedWarsGame game = victim.getGame();
        if (game == null || game.getState() != GameState.IN_PROGRESS) {
            return;
        }

        Entity source = event.source();
        if (source instanceof ProjectileEntity projectile) {
            source = projectile.getShooter();
        }

        if (source instanceof BedWarsPlayer attacker && event.dealt() > 0) {
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
