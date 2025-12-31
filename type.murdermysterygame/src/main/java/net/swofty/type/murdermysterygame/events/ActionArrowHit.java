package net.swofty.type.murdermysterygame.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.swofty.pvp.projectile.AbstractProjectile;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionArrowHit implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(ProjectileCollideWithEntityEvent event) {
        Entity projectile = event.getEntity();
        Entity target = event.getTarget();

        if (!(projectile instanceof AbstractProjectile arrow)) return;
        if (!(target instanceof MurderMysteryPlayer victim)) return;

        Entity shooter = arrow.getShooter();
        if (!(shooter instanceof MurderMysteryPlayer attacker)) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(attacker);
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }

        if (victim.isEliminated() || attacker.isEliminated()) {
            event.setCancelled(true);
            return;
        }

        // Don't shoot yourself
        if (attacker.getUuid().equals(victim.getUuid())) {
            event.setCancelled(true);
            return;
        }

        // Process the kill (bow kill)
        game.onPlayerKill(attacker, victim, Game.KillType.BOW);
    }
}
