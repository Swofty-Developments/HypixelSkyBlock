package net.swofty.type.murdermysterygame.events;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.item.Material;
import net.swofty.type.murdermysterygame.TypeMurderMysteryGameLoader;
import net.swofty.type.murdermysterygame.game.Game;
import net.swofty.type.murdermysterygame.game.GameStatus;
import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

import java.time.Duration;

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

        // Allow gold dropping - create shared ItemEntity that anyone can pick up
        if (material == Material.GOLD_INGOT) {
            ItemEntity itemEntity = new ItemEntity(event.getItemStack());
            itemEntity.setInstance(
                    player.getInstance(),
                    player.getPosition().add(0, player.getEyeHeight(), 0)
            );
            itemEntity.setVelocity(
                    player.getPosition().add(0, 0.3, 0).direction().mul(6)
            );
            itemEntity.setPickupDelay(Duration.ofMillis(500));
            return;
        }

        // Prevent dropping weapons
        if (material == Material.BOW || material == Material.ARROW || material == Material.IRON_SWORD) {
            event.setCancelled(true);
        }
    }
}
