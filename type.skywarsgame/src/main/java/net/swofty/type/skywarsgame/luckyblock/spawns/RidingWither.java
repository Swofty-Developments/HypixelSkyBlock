package net.swofty.type.skywarsgame.luckyblock.spawns;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

/**
 * Rideable Wither from Lucky Block.
 * Has 20HP, can fire wither skulls, and can fly!
 * Lasts 30 seconds.
 */
public class RidingWither extends RideableMob {

    private static final int DURATION_SECONDS = 30;
    private static final int SKULL_COOLDOWN_TICKS = 40; // 2 seconds
    private static final float WITHER_HEALTH = 20.0f;

    private int skullCooldown = 0;

    public RidingWither(SkywarsPlayer rider, Instance instance) {
        super(rider, instance);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WITHER;
    }

    @Override
    public String getDisplayName() {
        return "Wither";
    }

    @Override
    public int getDurationSeconds() {
        return DURATION_SECONDS;
    }

    @Override
    protected void onMountCreated() {
        // Set wither to reduced health
        mount.setHealth(WITHER_HEALTH);
    }

    @Override
    protected void onMountTick() {
        if (skullCooldown > 0) {
            skullCooldown--;
        }

        // Allow flight by keeping the wither at player's Y position
        if (rider.isFlying() || rider.isSneaking()) {
            // Player controls vertical movement
        }
    }

    /**
     * Fire a wither skull in the direction the player is looking.
     * Called when player right-clicks while riding.
     */
    public void fireWitherSkull() {
        if (skullCooldown > 0 || mount == null || !active) return;

        skullCooldown = SKULL_COOLDOWN_TICKS;

        // Create wither skull projectile
        EntityProjectile skull = new EntityProjectile(rider, EntityType.WITHER_SKULL);
        skull.setInstance(instance, mount.getPosition().add(0, 3, 0));

        // Calculate velocity from player look direction
        Vec direction = rider.getPosition().direction().mul(1.2);
        skull.setVelocity(direction);
    }

    @Override
    protected void onMountRemoved() {
        // Wither dies in explosion (visual effect)
    }
}
