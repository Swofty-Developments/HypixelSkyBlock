package net.swofty.types.generic.event.actions.player.fall;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

import java.util.HashMap;

public class ActionPlayerFall implements SkyBlockEventClass {
    public static HashMap<SkyBlockPlayer, Integer> fallHeight = new HashMap<>();

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        Pos newPosition = event.getNewPosition();
        Pos currentPosition = player.getPosition();

        if (player.isFlying() || player.isCreative() || player.isInLaunchpad()) {
            fallHeight.put(player, currentPosition.blockY());
            return;
        }

        fallHeight.computeIfAbsent(player, k -> currentPosition.blockY());
        int currentHeight = fallHeight.get(player);

        if (newPosition.y() > currentPosition.y() && currentHeight < newPosition.blockY()) {
            fallHeight.put(player, newPosition.blockY());
        } else if (newPosition.y() == currentPosition.y()) {
            int fallDistance = currentHeight - newPosition.blockY();

            if (fallDistance > 4) {
                player.damage(DamageType.FALL, (float) ((fallDistance * 2) - 4));
            }

            fallHeight.put(player, currentPosition.blockY());
        }
    }
}
