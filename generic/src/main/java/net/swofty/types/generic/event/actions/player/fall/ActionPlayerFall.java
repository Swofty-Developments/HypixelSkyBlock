package net.swofty.types.generic.event.actions.player.fall;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

import java.util.HashMap;

@EventParameters(description = "For the purpose of simulating fall damage",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerFall extends SkyBlockEvent {
    public static HashMap<SkyBlockPlayer, Integer> fallHeight = new HashMap<>();

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerMoveEvent playerEvent = (PlayerMoveEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();
        Pos newPosition = playerEvent.getNewPosition();
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
