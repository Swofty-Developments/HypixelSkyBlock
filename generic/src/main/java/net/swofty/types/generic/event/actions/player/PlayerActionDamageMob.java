package net.swofty.types.generic.event.actions.player;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.DamageIndicator;

import java.util.Map;

@EventParameters(description = "For damage indicators",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class PlayerActionDamageMob extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerEntityInteractEvent event = (PlayerEntityInteractEvent) tempEvent;
        if (event.getTarget().getEntityType().equals(EntityType.PLAYER)) return;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        Entity targetEntity = event.getTarget();
        SkyBlockMob mob;
        if (event.getTarget() instanceof SkyBlockMob skyBlockMob)
            mob = skyBlockMob;
        else return;
        LivingEntity livingEntity = (LivingEntity) targetEntity;

        ItemStatistics entityStats = mob.getStatistics();
        Map.Entry<Double, Boolean> hit = player.getStatistics().runPrimaryDamageFormula(entityStats);

        double damage = hit.getKey();
        boolean crit = hit.getValue();

        new DamageIndicator()
                .damage((float) damage)
                .pos(targetEntity.getPosition())
                .critical(crit)
                .display(targetEntity.getInstance());

        livingEntity.setVelocity(player.getPosition().asVec().sub(targetEntity.getPosition()).normalize().mul(0.5f));
        livingEntity.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) damage));
    }
}
