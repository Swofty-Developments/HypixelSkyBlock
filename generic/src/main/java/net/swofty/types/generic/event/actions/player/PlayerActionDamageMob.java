package net.swofty.types.generic.event.actions.player;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.DamageIndicator;

import java.util.Map;

@EventParameters(description = "For damage indicators",
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
        LivingEntity livingEntity = (LivingEntity) targetEntity;

        ItemStatistics entityStats = mob.getStatistics();
        Map.Entry<Double, Boolean> hit = player.getStatistics().runPrimaryDamageFormula(entityStats, player, livingEntity);

        double damage = hit.getKey();
        boolean critical = hit.getValue();

        new DamageIndicator()
                .damage((float) damage)
                .pos(targetEntity.getPosition())
                .critical(critical)
                .display(targetEntity.getInstance());

        livingEntity.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) damage));
    }
}
