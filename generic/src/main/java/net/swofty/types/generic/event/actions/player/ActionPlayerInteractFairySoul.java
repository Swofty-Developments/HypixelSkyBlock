package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.entity.EntityFairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.event.SkyBlockEvent;

public class ActionPlayerInteractFairySoul extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerEntityInteractEvent playerEvent = (PlayerEntityInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();

        if (playerEvent.getTarget() instanceof EntityFairySoul entitySoul) {
            FairySoul fairySoul = entitySoul.parent;
            if (fairySoul == null) return;

            fairySoul.collect(player);
        }
    }
}
