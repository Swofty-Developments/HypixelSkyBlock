package net.swofty.type.murdermysterygame.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.damage.DamageType;
import net.swofty.pvp.events.FinalDamageEvent;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPreventFallDamage implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(FinalDamageEvent event) {
        if (!(event.getEntity() instanceof MurderMysteryPlayer player)) return;

        // Allow damage in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;

        // Check if this is fall damage
        DamageType damageType = event.getDamage().getType().asValue();
        if (damageType == DamageType.FALL.asValue()) {
            event.setCancelled(true);
        }
    }
}
