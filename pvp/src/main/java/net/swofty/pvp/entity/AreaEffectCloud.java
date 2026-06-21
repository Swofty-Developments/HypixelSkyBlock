package net.swofty.pvp.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.Aerodynamics;
import net.minestom.server.color.AlphaColor;
import net.minestom.server.color.Color;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.AreaEffectCloudMeta;
import net.minestom.server.instance.EntityTracker;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.particle.Particle;
import net.swofty.pvp.feature.effect.EffectFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AreaEffectCloud extends Entity {
    protected int duration;
    protected final int durationOnUse;
    protected final float radiusPerTick;
    protected final float radiusOnUse;
    protected final int reapplicationDelay;
    protected final int waitTime;
    private final AreaEffectCloudMeta meta;

    protected int age = 0;
    private final Map<@NotNull LivingEntity, @NotNull Integer> affectedEntities = new HashMap<>();
    private final UUID ownerUuid;
    private final PotionContents potionContents;

    private final EffectFeature effectFeature;

    private static final int DEFAULT_DURATION_ON_USE = 0;
    private static final float DEFAULT_RADIUS_ON_USE = 0.0F;
    private static final float DEFAULT_RADIUS_PER_TICK = 0.0F;
    private static final float MINIMAL_RADIUS = 0.5F;
    private static final float DEFAULT_RADIUS = 3.0F;
    private static final float MAX_RADIUS = 32.0f;
    public static final int INFINITE_DURATION = -1;
    public static final int DEFAULT_LINGERING_DURATION = 600;
    private static final int DEFAULT_WAIT_TIME = 20;
    private static final int DEFAULT_REAPPLICATION_DELAY = 20;
    private static final Particle.EntityEffect DEFAULT_PARTICLE =
        new Particle.EntityEffect(Particle.ENTITY_EFFECT.key(), Particle.ENTITY_EFFECT.id(), new AlphaColor(-1));
    private final Set<LivingEntity> entitiesToAffect = new HashSet<>();

    public AreaEffectCloud(int duration, float radius, Particle particle, float radiusPerTick, float radiusOnUse, int reapplicationDelay,
                           int durationOnUse, int waitTime, PotionContents potionContents, @Nullable UUID ownerUuid,
                           EffectFeature effectFeature) {
        super(EntityType.AREA_EFFECT_CLOUD);
        this.duration = duration;
        this.radiusPerTick = radiusPerTick;
        this.radiusOnUse = radiusOnUse;
        this.reapplicationDelay = reapplicationDelay;
        this.durationOnUse = durationOnUse;
        this.waitTime = waitTime;

        meta = (AreaEffectCloudMeta) this.getEntityMeta();
        meta.setWaiting(age < waitTime);
        meta.setRadius(Math.min(MAX_RADIUS, radius));
        meta.setParticle(particle);

        this.ownerUuid = ownerUuid;
        this.effectFeature = effectFeature;

        this.potionContents = potionContents;
        if (particle instanceof Particle.EntityEffect entityEffect && entityEffect.color().asARGB() == -1)
            updateColor(this.potionContents);

        this.setNoGravity(true);
        this.setAerodynamics(new Aerodynamics(0.0, 0.0, 0.0));
    }

    public AreaEffectCloud() {
        this(
            DEFAULT_LINGERING_DURATION,
            DEFAULT_RADIUS,
            DEFAULT_PARTICLE,
            DEFAULT_RADIUS_PER_TICK,
            DEFAULT_RADIUS_ON_USE,
            DEFAULT_REAPPLICATION_DELAY,
            DEFAULT_DURATION_ON_USE,
            DEFAULT_WAIT_TIME,
            PotionContents.EMPTY,
            null,
            EffectFeature.NO_OP
        );
    }

    @Override
    public void tick(long time) {
        super.tick(time);
        if (isRemoved()) return;

        age++;
        meta.setWaiting(age < waitTime);

        if (age >= waitTime + duration && duration != INFINITE_DURATION) {
            discard();
            return;
        }
        if (meta.isWaiting()) return;

        changeRadiusPerTick();

        if (age % 5 == 0) {
            affectedEntities.entrySet().removeIf(entry -> age >= entry.getValue());

            if (potionContents == PotionContents.EMPTY) {
                affectedEntities.clear();
                return;
            }

            entitiesToAffect.clear();
            float radiusSquared = meta.getRadius() * meta.getRadius();
            instance.getEntityTracker().nearbyEntities(position, meta.getRadius(),
                EntityTracker.Target.ENTITIES, entity -> {
                    if (get2DDistanceSquared(entity) <= radiusSquared && entity instanceof LivingEntity livingEntity)
                        entitiesToAffect.add(livingEntity);
                });

            Player attacker = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(ownerUuid);
            entitiesToAffect.forEach(entity -> {
                if (affectedEntities.containsKey(entity)) return;

                effectFeature.addLingeringPotionEffects(entity, potionContents, this, attacker);
                changeRadiusOnUse();
                changeDurationOnUse();
                affectedEntities.put(entity, age + reapplicationDelay);
            });

        }
    }

    private void discard() {
        scheduler().scheduleEndOfTick(this::remove);
    }

    private void changeRadiusPerTick() {
        float calculatedRadius = meta.getRadius();
        if (radiusPerTick != DEFAULT_RADIUS_PER_TICK) {
            calculatedRadius += radiusPerTick;
            if (calculatedRadius < MINIMAL_RADIUS) {
                discard();
                return;
            }
            meta.setRadius(Math.min(MAX_RADIUS, calculatedRadius));
        }
    }

    private void changeRadiusOnUse() {
        float calculatedRadius = meta.getRadius() + radiusOnUse;
        if (calculatedRadius < MINIMAL_RADIUS) {
            discard();
            return;
        }
        meta.setRadius(Math.min(MAX_RADIUS, calculatedRadius));
    }

    private void changeDurationOnUse() {
        duration = duration + durationOnUse;
        if (duration <= 0) discard();
    }

    private double get2DDistanceSquared(Entity entity) {
        double dx = position.x() - entity.getPosition().x();
        double dz = position.z() - entity.getPosition().z();
        return (dx * dx) + (dz * dz);
    }

    public void updateColor(PotionContents potionContents) {
        if (meta.getParticle() instanceof Particle.EntityEffect particle) {
            Color color = new Color(effectFeature.getPotionColor(potionContents));
            meta.setParticle(
                new Particle.EntityEffect(
                    particle.key(), particle.id(), new AlphaColor(255, color)));
        }
    }

    public void updateColor(AlphaColor alphaColor) {
        if (meta.getParticle() instanceof Particle.EntityEffect particle) {
            meta.setParticle(new Particle.EntityEffect(
                particle.key(), particle.id(), alphaColor));
        }
    }
}