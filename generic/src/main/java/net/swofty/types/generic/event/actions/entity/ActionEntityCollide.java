package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityTickEvent;
import net.minestom.server.event.entity.EntityVelocityEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.entity.ArrowEntityImpl;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles Entity collision",
        node = EventNodes.ENTITY,
        requireDataLoaded = false)
public class ActionEntityCollide extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return EntityTickEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        EntityTickEvent event = (EntityTickEvent) tempEvent;
        if (event.getEntity() instanceof DroppedItemEntityImpl) return;
        if (event.getEntity() instanceof ArrowEntityImpl) return;
        if (event.getEntity() instanceof Player) return;

        PhysicsResult physResults = CollisionUtils.checkEntityCollisions(event.getEntity(), event.getEntity().getVelocity());
        event.getEntity().setVelocity(physResults.newVelocity());
    }
}
