package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityVelocityEvent;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.EntityCollisionUtils;

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

        Vec toMove = EntityCollisionUtils.calculateEntityCollisions(event.getEntity());

        event.setVelocity(toMove);
    }
}
