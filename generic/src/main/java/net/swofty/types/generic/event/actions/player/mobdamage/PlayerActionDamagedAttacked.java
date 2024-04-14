package net.swofty.types.generic.event.actions.player.mobdamage;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.EntityDamage;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.PlayerStatistics;
import net.swofty.types.generic.utility.DamageIndicator;

import java.util.Map;

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
            if (mob.getLastAttack() + mob.damageCooldown() > System.currentTimeMillis()) return;
            mob.setLastAttack(System.currentTimeMillis());

            ItemStatistics mobStatistics = mob.getStatistics();
            ItemStatistics playerStatistics = ((SkyBlockPlayer) event.getTarget()).getStatistics().allStatistics();

            Map.Entry<Double, Boolean> damageDealt =
                    PlayerStatistics.runPrimaryDamageFormula(mobStatistics, playerStatistics);

            PlayerDamagedByMobValueUpdateEvent valueEvent = new PlayerDamagedByMobValueUpdateEvent(
                    (SkyBlockPlayer) event.getTarget(), damageDealt.getKey().floatValue(), mob);
            SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

            ((SkyBlockPlayer) event.getTarget()).damage(new EntityDamage(mob, (float) valueEvent.getValue()));

            new DamageIndicator()
                    .damage((float) valueEvent.getValue())
                    .pos(event.getTarget().getPosition())
                    .critical(damageDealt.getValue())
                    .display(event.getTarget().getInstance());
        }
    }

}
