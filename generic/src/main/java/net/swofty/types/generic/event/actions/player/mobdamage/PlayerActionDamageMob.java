package net.swofty.types.generic.event.actions.player.mobdamage;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.damage.EntityDamage;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.events.PlayerDamageMobValueUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.DamageIndicator;

import java.util.Map;

@EventParameters(description = "For mobdamage indicators",
        node = EventNodes.ALL,
        requireDataLoaded = false)
public class PlayerActionDamageMob extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return EntityAttackEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        EntityAttackEvent event = (EntityAttackEvent) tempEvent;
        if (event.getTarget().getEntityType().equals(EntityType.PLAYER)) return;
        if (!event.getEntity().getEntityType().equals(EntityType.PLAYER)) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getEntity();

        Entity targetEntity = event.getTarget();
        SkyBlockMob mob;
        if (event.getTarget() instanceof SkyBlockMob skyBlockMob)
            mob = skyBlockMob;
        else return;
        LivingEntity targetLivingEntity = (LivingEntity) targetEntity;

        ItemStatistics entityStats = mob.getStatistics();
        Map.Entry<Double, Boolean> hit = player.getStatistics().runPrimaryDamageFormula(entityStats, player, targetLivingEntity);

        double damage = hit.getKey();
        boolean critical = hit.getValue();

        PlayerDamageMobValueUpdateEvent valueEvent = new PlayerDamageMobValueUpdateEvent(
                (SkyBlockPlayer) event.getEntity(), (float) damage, mob);
        SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

        new DamageIndicator()
                .damage((float) valueEvent.getValue())
                .pos(targetEntity.getPosition())
                .critical(critical)
                .display(targetEntity.getInstance());

        targetLivingEntity.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) valueEvent.getValue()));
    }
}
