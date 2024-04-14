package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.network.packet.server.play.DamageEventPacket;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles Entity mobdamage animations",
        node = EventNodes.ENTITY,
        requireDataLoaded = false)
public class ActionAnimateEntityDamage extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return EntityDamageEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        EntityDamageEvent event = (EntityDamageEvent) tempEvent;
        event.setAnimation(true);
        event.setCancelled(false);

        event.getEntity().sendPacketToViewersAndSelf(new DamageEventPacket(event.getEntity().getEntityId(),
                        0,
                        0,
                        0,
                        null));
    }
}
