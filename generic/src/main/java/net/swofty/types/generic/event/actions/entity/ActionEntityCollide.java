package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityVelocityEvent;
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
        return EntityVelocityEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        EntityVelocityEvent event = (EntityVelocityEvent) tempEvent;
        if (event.getEntity() instanceof DroppedItemEntityImpl) return;
        if (event.getEntity() instanceof ArrowEntityImpl) return;
        if (event.getEntity() instanceof Player) return;

        PhysicsResult physResults = CollisionUtils.checkEntityCollisions(event.getEntity(), event.getVelocity());
        event.setVelocity(physResults.newVelocity());
    }
}
