package net.swofty.type.skyblockgeneric.event.actions.player.fall;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerFall implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        Pos newPosition = event.getNewPosition();
        Pos currentPosition = player.getPosition();

        if (player.isFlying() || player.getGameMode().equals(GameMode.CREATIVE) || player.isInLaunchpad()) {
            player.setFallHeight(currentPosition.blockY());
            return;
        }

        Integer currentHeight = player.getFallHeight();
        if (currentHeight == null) {
            currentHeight = currentPosition.blockY();
        }

        if (newPosition.y() > currentPosition.y() && currentHeight < newPosition.blockY()) {
            player.setFallHeight(newPosition.blockY());
            return;
        }

        if (player.isOnGround()) {
            int fallDistance = currentHeight - newPosition.blockY();
            if (fallDistance > 4) {
                player.damage(DamageType.FALL, (float) ((fallDistance * 2) - 4));
            }

            player.setFallHeight(newPosition.blockY());
        }
    }
}
