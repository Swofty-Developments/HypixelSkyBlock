package net.swofty.type.skyblockgeneric.entity;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.collision.EntityCollisionResult;
import net.minestom.server.collision.PhysicsResult;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePotionData;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkills;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.potion.PotionEffectService;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Entity for thrown splash potions.
 * Handles collision detection and applies effects on impact.
 */
public class SplashPotionEntity extends Entity {
    private static final double SPLASH_RADIUS = 4.0;
    private static final double GRAVITY = 0.05;
    private static final double AIR_RESISTANCE = 0.99;

    @Getter
    private final SkyBlockPlayer thrower;
    @Getter
    private final SkyBlockItem potionItem;
    @Getter
    private final ItemAttributePotionData.PotionData potionData;
    @Getter
    private final int throwerAlchemyLevel;

    private boolean hasImpacted = false;
    private Vec velocity;

    public SplashPotionEntity(SkyBlockPlayer thrower, SkyBlockItem potionItem) {
        super(EntityType.SPLASH_POTION);

        this.thrower = thrower;
        this.potionItem = potionItem;
        this.potionData = potionItem.getAttributeHandler().getPotionData();

        // Get thrower's alchemy level for duration calculation
        DatapointSkills.PlayerSkills skills = thrower.getSkills();
        this.throwerAlchemyLevel = skills.getCurrentLevel(SkillCategories.ALCHEMY);

        // Calculate initial velocity based on player's look direction
        Pos playerPos = thrower.getPosition();
        double yaw = Math.toRadians(playerPos.yaw());
        double pitch = Math.toRadians(playerPos.pitch());

        double power = 0.5; // Throw power
        double vx = -Math.sin(yaw) * Math.cos(pitch) * power;
        double vy = -Math.sin(pitch) * power + 0.2; // Add upward arc
        double vz = Math.cos(yaw) * Math.cos(pitch) * power;

        this.velocity = new Vec(vx, vy, vz);
    }

    @Override
    public void spawn() {
        super.spawn();

        // Auto-remove after 5 seconds if it somehow doesn't hit anything
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (!isRemoved() && !hasImpacted) {
                impact();
            }
        }, TaskSchedule.tick(100), TaskSchedule.stop());
    }

    @Override
    public void tick(long time) {
        if (hasImpacted || isRemoved()) return;

        final Pos posBefore = getPosition();
        super.tick(time);

        // Apply gravity
        velocity = velocity.add(0, -GRAVITY, 0);

        // Apply air resistance
        velocity = velocity.mul(AIR_RESISTANCE);

        // Calculate new position
        Pos newPos = posBefore.add(velocity.x(), velocity.y(), velocity.z());

        // Check for block collisions
        PhysicsResult result = CollisionUtils.handlePhysics(
                instance, this.getChunk(),
                this.getBoundingBox(),
                posBefore, velocity,
                null, true
        );

        // Check for entity collisions
        @NotNull Collection<EntityCollisionResult> collided = CollisionUtils.checkEntityCollisions(
                instance, this.getBoundingBox(), posBefore, velocity, 3,
                (e) -> e != this && e != thrower, result
        );

        // If hit an entity, impact there
        for (EntityCollisionResult collisionResult : collided) {
            if (collisionResult != null && collisionResult.entity() != thrower) {
                Entity entity = collisionResult.entity();
                // Don't collide with armor stands
                if (entity.getEntityType() == EntityType.ARMOR_STAND) {
                    continue;
                }
                teleport(Pos.fromPoint(collisionResult.collisionPoint()));
                impact();
                return;
            }
        }

        // If hit a block, impact there
        if (result.hasCollision()) {
            teleport(result.newPosition());
            impact();
            return;
        }

        // Update position
        float yaw = (float) Math.toDegrees(Math.atan2(velocity.x(), velocity.z()));
        float pitch = (float) Math.toDegrees(Math.atan2(velocity.y(), Math.sqrt(velocity.x() * velocity.x() + velocity.z() * velocity.z())));
        teleport(new Pos(newPos.x(), newPos.y(), newPos.z(), yaw, pitch));
    }

    /**
     * Called when the potion impacts a surface or entity
     */
    private void impact() {
        if (hasImpacted) return;
        hasImpacted = true;

        Pos impactPos = getPosition();

        // Play splash particles
        spawnSplashParticles(impactPos);

        // Apply effects to nearby entities
        applyEffectsInRadius(impactPos);

        // Remove the entity
        remove();
    }

    /**
     * Spawn splash particles at impact location
     */
    private void spawnSplashParticles(Pos pos) {
        ParticlePacket packet = new ParticlePacket(
                Particle.EFFECT,
                pos.x(), pos.y() + 0.5, pos.z(),
                0.5f, 0.5f, 0.5f,
                0.1f, 100
        );

        // Send to nearby players
        for (Entity entity : instance.getNearbyEntities(pos, 32)) {
            if (entity instanceof Player player) {
                player.sendPacket(packet);
            }
        }
    }

    /**
     * Apply potion effects to all entities within splash radius
     */
    private void applyEffectsInRadius(Pos center) {
        if (potionData == null) return;

        for (Entity entity : instance.getNearbyEntities(center, SPLASH_RADIUS)) {
            if (entity == this) continue;

            double distance = entity.getPosition().distance(center);
            if (distance > SPLASH_RADIUS) continue;

            if (entity instanceof SkyBlockPlayer player) {
                // Apply to players (including thrower)
                PotionEffectService.applySplashToPlayer(player, potionData, throwerAlchemyLevel, distance);
            } else if (entity instanceof SkyBlockMob mob) {
                // Apply to mobs
                PotionEffectService.applySplashToMob(mob, potionData, throwerAlchemyLevel, distance);
            }
        }
    }
}
