package net.swofty.types.generic.event.actions.player.mobdamage;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.events.PlayerDamageMobValueUpdateEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.utility.DamageIndicator;
import net.swofty.types.generic.utility.MathUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventParameters(description = "For damage indicators",
        node = EventNodes.ALL,
        requireDataLoaded = false)
public class PlayerActionDamageMob extends SkyBlockEvent {
    private static final Random random = new Random();
    private static final Map<UUID, Long> COOLDOWN = new HashMap<>();

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

        player.playSound(Sound.sound(Key.key("entity." + mob.getEntityType().name().toLowerCase().replace("minecraft:", "") + ".hurt"), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());

        ItemStatistics entityStats = mob.getStatistics();
        Map.Entry<Double, Boolean> hit = player.getStatistics().runPrimaryDamageFormula(entityStats, player, targetLivingEntity);

        double damage = hit.getKey();
        boolean critical = hit.getValue();

        PlayerDamageMobValueUpdateEvent valueEvent = new PlayerDamageMobValueUpdateEvent(
                (SkyBlockPlayer) event.getEntity(), (float) damage, mob);
        SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

        if (COOLDOWN.containsKey(player.getUuid()) && System.currentTimeMillis() < COOLDOWN.get(player.getUuid()))
                return;
        COOLDOWN.put(player.getUuid(), System.currentTimeMillis() +
                MathUtility.ticksToMilliseconds(player.getStatistics().getInvulnerabilityTime()));

        new DamageIndicator()
                .damage((float) valueEvent.getValue())
                .pos(targetEntity.getPosition())
                .critical(critical)
                .display(targetEntity.getInstance());

        player.playSound(Sound.sound(Key.key("entity." + mob.getEntityType().name().toLowerCase().replace("minecraft:", "") + ".hurt"), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());

        targetLivingEntity.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) valueEvent.getValue()));

        double ferocity = player.getStatistics().allStatistics().getOverall(ItemStatistic.FEROCITY);
        int extraAttacks = (int) (ferocity / 100);
        double extraAttackChance = (ferocity % 100) / 100.0;

        // Extra attacks that are guaranteed because ferocity overflowed 100
        for (int i = 0; i < extraAttacks; i++) {
            targetLivingEntity.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) valueEvent.getValue()));
        }

        // Extra attacks that have a chance to occur based on the remaining ferocity
        if (random.nextDouble() < extraAttackChance) {
            targetLivingEntity.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) valueEvent.getValue()));
        }
    }
}