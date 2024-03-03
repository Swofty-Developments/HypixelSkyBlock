package net.swofty.types.generic.event.actions.player.fall;

import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "For detecting player fall into void",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerVoid extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerMoveEvent playerEvent = (PlayerMoveEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();

        if (player.getPosition().y() <= -1) {
            player.damage(DamageType.OUT_OF_WORLD, 1000000000);
        }
    }
}
