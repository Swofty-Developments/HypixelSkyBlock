package net.swofty.type.murdermysterygame.events;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.item.Material;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionGoldDrop implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(ItemDropEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;

        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }

        Material material = event.getItemStack().material();

        if (material == Material.GOLD_INGOT) {
            event.setCancelled(true);
            return;
        }

        // Prevent dropping weapons
        if (material == Material.BOW || material == Material.ARROW || material == Material.IRON_SWORD) {
            event.setCancelled(true);
        }
    }
}
