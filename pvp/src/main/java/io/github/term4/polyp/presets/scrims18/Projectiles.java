package io.github.term4.polyp.presets.scrims18;

import io.github.term4.polyp.mechanics.projectile.ProjectileConfig;
import io.github.term4.polyp.mechanics.projectile.types.Arrow;
import io.github.term4.polyp.mechanics.projectile.types.FishingBobber;
import io.github.term4.polyp.mechanics.projectile.types.ProjectileTypeConfig;
import io.github.term4.polyp.presets.vanilla18.Vanilla18;

/**
 * Scrims 1.8 projectiles: the {@link Vanilla18} 1.8 baseline with the tracker muted - a projectile is spawned,
 * handed its launch velocity, and never corrected again.
 *
 * <p>Capture-verified (scrims18snowballbow + scrims18moreprojectiles, 141 spawns over snowball/arrow/pearl/
 * bobber): not one {@code entity_teleport} or {@code entity_relative_move} on ANY projectile, and no velocity
 * after the launch one. That launch velocity rides {@code spawn_object} where 1.8 has room for it (arrow and
 * bobber carry objectData = shooter id, so those spawn alone) and needs a separate {@code entity_velocity}
 * where it does not (snowball and pearl carry objectData 0, whose spawn packet omits the velocity fields).
 * Either way the client predicts the whole arc, which is what {@code syncInterval(0)} +
 * {@code velocitySyncInterval(0)} produce here.
 *
 * <p>Launch spread is GONE, the usual competitive patch to 1.8's {@code shoot()}. Held-aim captures collapse
 * onto one velocity vector per aim with the off-axis component a hard zero - 128 snowballs onto 4
 * (scrims18staticsnowballtest), 40 rod casts onto 1 (scrims18staticrod) - where vanilla's gaussian would
 * scatter every single launch. Both ends of the launch path, so it sits on the shared baseline.
 *
 * <p>They also launch from the middle of the view, not off the shoulder: 1.8 steps the spawn 0.16 sideways
 * ({@code locX -= cos(yaw) * 0.16F}), and across 95 held-aim spawns that sideways component is a hard zero,
 * traded for a push straight out along the aim ({@link #SPAWN_FORWARD}).
 *
 * <p>Launch speed is short of 1.8's too - see {@link #THROW_SPEED} and {@link #ROD_SPEED}. Flight physics stay
 * vanilla: with no follow-up packets there is no second sample to difference, so these captures can neither
 * confirm nor move gravity/drag.
 */
public final class Projectiles {

    private Projectiles() {}

    // scrims18staticsnowballtest, 128 throws over 4 held aims, wire velocities +-(11962,-74,0) and
    // +-(11965,-69,0): |v| lands 1.4952-1.4959, a quantum either side of this. Paper 1.8's EntityProjectile.j()
    // returns 1.5F flat, so the 0.3% is theirs.
    private static final double THROW_SPEED = 1.4955;
    // scrims18staticrod, 40 casts from a held aim: 39 of them the same (11723,-87,0). Well under the 1.5F
    // EntityFishingHook launches with, and 0.98x the throwables - the rod is nerfed on its own, not globally.
    private static final double ROD_SPEED = 1.4655;

    // out along the view instead of 1.8's step to the side. The two static captures face opposite ways, so
    // their floor-quantized readings bracket it from both sides: 0.40625 < forward < 0.4375. 0.425 is the
    // player's half-width plus the projectile's (0.3 + 0.125) - it clears the thrower's box exactly.
    private static final double SPAWN_FORWARD = 0.425;
    // same captures: spawn y sits a wire step above 1.8's headHeight-0.1, bracketing eye-0.089 .. eye-0.058
    private static final double SPAWN_VERTICAL = -0.08;

    /** Vanilla 1.8 projectiles with the periodic wire sync turned off on every type. */
    public static ProjectileConfig config() {
        ProjectileConfig base = Vanilla18.projectiles();
        return ProjectileConfig.builder(base)
                // generic base covers snowball / egg / pearl (they inherit both knobs), and carries the no-spread
                // patch down to arrow + bobber, whose entries never set spread themselves. Arrow and splash pin
                // their own speed, so THROW_SPEED reaches only the types it was measured on.
                .defaults(silent(ProjectileTypeConfig.builder(base.defaults())
                        .spread(0.0).speed(THROW_SPEED)
                        .spawnOffsetForward(SPAWN_FORWARD)
                        .spawnOffsetSideways(0.0)
                        .spawnOffsetVertical(SPAWN_VERTICAL)))
                // arrow + bobber set their own interval, so silence them explicitly
                .typeConfigs(
                        silent(ProjectileTypeConfig.builder(base.typeConfig(Arrow.KEY))),
                        silent(ProjectileTypeConfig.builder(base.typeConfig(FishingBobber.KEY)).speed(ROD_SPEED)))
                .build();
    }

    private static ProjectileTypeConfig silent(ProjectileTypeConfig.Builder b) {
        return b.syncInterval(0).velocitySyncInterval(0).build();
    }
}
