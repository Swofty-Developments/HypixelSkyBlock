package net.swofty.types.generic.event.actions.entity;

import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.network.packet.server.play.DamageEventPacket;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;

public class ActionAnimateEntityDamage implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.ENTITY , requireDataLoaded = false)
    public void run(EntityDamageEvent event) {
        event.setAnimation(true);
        event.setCancelled(false);

        event.getEntity().sendPacketToViewersAndSelf(new DamageEventPacket(event.getEntity().getEntityId(),
                        0,
                        0,
                        0,
                        null));
    }
}
