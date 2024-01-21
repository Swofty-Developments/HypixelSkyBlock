package net.swofty.types.generic.event.actions.player;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.swofty.types.generic.utility.DamageIndicator;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "For damage indicators",
        node = EventNodes.ENTITY,
        requireDataLoaded = true)
public class PlayerActionDamage extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return EntityDamageEvent.class;
    }

    /*
    TODO:
          Implement the actual damage system to determine when the player crits an entity
          * to be done after the entity system
     */
    @Override
    public void run(Event event) {
        EntityDamageEvent e = (EntityDamageEvent) event;
        if (!e.getEntity().getEntityType().equals(EntityType.PLAYER)) return;

        float damageDealt = e.getDamage().getAmount();
        new DamageIndicator().damage(damageDealt).pos(e.getEntity().getPosition()).display(e.getEntity().getInstance());
    }
}
