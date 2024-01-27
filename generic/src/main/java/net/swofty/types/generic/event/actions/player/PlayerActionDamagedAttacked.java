package net.swofty.types.generic.event.actions.player;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.damage.EntityDamage;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.utility.DamageIndicator;

@EventParameters(description = "For damage indicators",
        node = EventNodes.ENTITY,
        requireDataLoaded = false)
public class PlayerActionDamagedAttacked extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return EntityAttackEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        EntityAttackEvent event = (EntityAttackEvent) tempEvent;
        if (!event.getTarget().getEntityType().equals(EntityType.PLAYER)) return;

        if (event.getEntity() instanceof SkyBlockMob mob) {
            float damageDealt = mob.getStatistics().get(ItemStatistic.DAMAGE).floatValue();

            PlayerDamagedByMobValueUpdateEvent valueEvent = new PlayerDamagedByMobValueUpdateEvent(
                    (SkyBlockPlayer) event.getTarget(), damageDealt, mob);
            SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

            ((SkyBlockPlayer) event.getTarget()).damage(new EntityDamage(mob, (float) valueEvent.getValue()));
        }
    }

}
