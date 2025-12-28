package net.swofty.type.bedwarsgame.events;

import net.minestom.server.tag.Tag;
import net.swofty.pvp.events.FinalDamageEvent;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.death.BedWarsCombatTracker;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.game.GameStatus;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionGameCombatTrack implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ENTITY, requireDataLoaded = false)
    public void run(FinalDamageEvent event) {
        if (!(event.getEntity() instanceof BedWarsPlayer victim)) {
            return;
        }

        if (!victim.hasTag(Tag.String("gameId"))) {
            return;
        }

        String gameId = victim.getTag(Tag.String("gameId"));
        Game game = TypeBedWarsGameLoader.getGameById(gameId);

        if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) {
            return;
        }

        if (event.getDamage().getAttacker() instanceof BedWarsPlayer attacker) {
            if (!victim.equals(attacker) && !isSameTeam(victim, attacker)) {
                BedWarsCombatTracker.recordAttack(victim, attacker);
            }
        }
    }

    private boolean isSameTeam(BedWarsPlayer player1, BedWarsPlayer player2) {
        String team1 = player1.getTag(Tag.String("team"));
        String team2 = player2.getTag(Tag.String("team"));
        return team1 != null && team1.equals(team2);
    }
}

