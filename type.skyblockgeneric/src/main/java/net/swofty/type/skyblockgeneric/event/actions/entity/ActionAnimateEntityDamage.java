package net.swofty.type.skyblockgeneric.event.actions.entity;

import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.network.packet.server.play.DamageEventPacket;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;

public class ActionAnimateEntityDamage implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.ENTITY , requireDataLoaded = false)
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
