package net.swofty.type.murdermysterygame.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionPreventBlockModification implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockBreak(PlayerBlockBreakEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        // Allow in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            event.setCancelled(true);
        }
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        // Allow in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            event.setCancelled(true);
        }
    }
}
