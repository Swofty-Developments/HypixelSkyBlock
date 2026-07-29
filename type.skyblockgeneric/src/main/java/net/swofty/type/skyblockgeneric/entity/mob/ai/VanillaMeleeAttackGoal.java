package net.swofty.type.skyblockgeneric.entity.mob.ai;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.generic.entity.ai.vanilla.VanillaNavigator;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

/**
 * Drop-in replacement for Minestom's {@code MeleeAttackGoal} that approaches and attacks the mob's
 * target, but drives movement through the mob's
 * {@link net.swofty.type.generic.entity.ai.vanilla.VanillaNavigator} (our own A* + vanilla follow loop)
 * rather than Minestom's built-in navigator.
 */
public class VanillaMeleeAttackGoal extends GoalSelector {

    private final Cooldown cooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));

    private long lastHit;
    private final double range;
    private final Duration delay;

    private boolean stop;
    private Entity cachedTarget;

    /**
     * @param entityCreature the entity to add the goal to
     * @param range          the allowed range the entity can attack others.
     * @param delay          the delay between each attacks
     * @param timeUnit       the unit of the delay
     */
    public VanillaMeleeAttackGoal(@NotNull EntityCreature entityCreature, double range, int delay, @NotNull TemporalUnit timeUnit) {
        this(entityCreature, range, Duration.of(delay, timeUnit));
    }

    /**
     * @param entityCreature the entity to add the goal to
     * @param range          the allowed range the entity can attack others.
     * @param delay          the delay between each attacks
     */
    public VanillaMeleeAttackGoal(@NotNull EntityCreature entityCreature, double range, Duration delay) {
        super(entityCreature);
        this.range = range;
        this.delay = delay;
    }

    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public boolean shouldStart() {
        this.cachedTarget = findTarget();
        return this.cachedTarget != null;
    }

    @Override
    public void start() {
        final Point targetPosition = this.cachedTarget.getPosition();
        ((SkyBlockMob) entityCreature).getVanillaNavigator().pathTo(targetPosition);
    }

    @Override
    public void tick(long time) {
        Entity target;
        if (this.cachedTarget != null) {
            target = this.cachedTarget;
            this.cachedTarget = null;
        } else {
            target = findTarget();
        }

        this.stop = target == null;

        if (!stop) {

            // Attack the target entity
            if (entityCreature.getDistanceSquared(target) <= range * range) {
                entityCreature.lookAt(target);
                if (!Cooldown.hasCooldown(time, lastHit, delay)) {
                    entityCreature.attack(target, true);
                    this.lastHit = time;
                }
                return;
            }

            // Move toward the target entity
            VanillaNavigator navigator = ((SkyBlockMob) entityCreature).getVanillaNavigator();
            final var pathPosition = navigator.getTargetPosition();
            final var targetPosition = target.getPosition();
            if (pathPosition == null || !pathPosition.samePoint(targetPosition) || navigator.isComplete()) {
                if (this.cooldown.isReady(time)) {
                    this.cooldown.refreshLastUpdate(time);
                    navigator.pathTo(targetPosition);
                }
            }
        }
    }

    @Override
    public boolean shouldEnd() {
        return stop;
    }

    @Override
    public void end() {
        // Stop following the target
        ((SkyBlockMob) entityCreature).getVanillaNavigator().stop();
    }
}
