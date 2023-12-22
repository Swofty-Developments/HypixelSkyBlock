package net.swofty.event.actions.player.fall;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.DamageIndicator;

@EventParameters(description = "For damage indicators",
        node = EventNodes.ENTITY,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = true)
public class PlayerActionDamage extends SkyBlockEvent
{
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

            float damageDealt = e.getDamage();
            new DamageIndicator().damage(damageDealt).pos(e.getEntity().getPosition()).display();
      }
}
