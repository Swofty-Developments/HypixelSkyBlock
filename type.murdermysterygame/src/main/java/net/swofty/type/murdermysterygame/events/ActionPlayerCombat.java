package net.swofty.type.murdermysterygame.events;

import net.minestom.server.item.Material;
import net.swofty.pvp.events.PrepareAttackEvent;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.role.GameRole;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPlayerCombat implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PrepareAttackEvent event) {
        if (!(event.getEntity() instanceof MurderMysteryPlayer attacker)) return;
        if (!(event.getTarget() instanceof MurderMysteryPlayer victim)) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(attacker);
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        // No combat in waiting lobby
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }

        // Check if victim is already eliminated
        if (victim.isEliminated()) {
            event.setCancelled(true);
            return;
        }

        // Check if attacker is eliminated (spectator)
        if (attacker.isEliminated()) {
            event.setCancelled(true);
            return;
        }

        GameRole attackerRole = game.getRoleManager().getRole(attacker.getUuid());
        if (attackerRole == null) {
            event.setCancelled(true);
            return;
        }

        // Only murderer with knife can melee kill
        if (attackerRole == GameRole.MURDERER) {
            // Check if holding knife (iron sword) and murderer has received their sword
            Material heldItem = attacker.getItemInMainHand().material();
            if (heldItem == Material.IRON_SWORD && game.hasMurdererReceivedSword()) {
                // Instant kill
                game.onPlayerKill(attacker, victim);
            }
            // Cancel the attack either way - murderer either kills instantly or does nothing
            event.setCancelled(true);
        } else {
            // Innocents and detectives cannot melee kill - they use bow
            // Cancel regular melee attacks
            event.setCancelled(true);
        }
    }
}
