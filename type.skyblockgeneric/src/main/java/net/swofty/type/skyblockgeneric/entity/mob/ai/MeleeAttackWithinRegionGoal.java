package net.swofty.type.skyblockgeneric.entity.mob.ai;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

public class MeleeAttackWithinRegionGoal extends GoalSelector {

    private final Cooldown cooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));

    private long lastHit;
    private final double range;
    private final Duration delay;
    private final RegionType type;

    private boolean stop;
    private Entity cachedTarget;

    private boolean lookAt = true;

    public MeleeAttackWithinRegionGoal(@NotNull EntityCreature entityCreature, double range, int delay, @NotNull TemporalUnit timeUnit, RegionType type) {
        this(entityCreature, range, Duration.of(delay, timeUnit), type);
    }

    public MeleeAttackWithinRegionGoal(@NotNull EntityCreature entityCreature, double range, int delay, @NotNull TemporalUnit timeUnit, RegionType type, boolean lookAt) {
        this(entityCreature, range, Duration.of(delay, timeUnit), type);
        this.lookAt = lookAt;
    }

    public MeleeAttackWithinRegionGoal(@NotNull EntityCreature entityCreature, double range, Duration delay, RegionType type) {
        super(entityCreature);
        this.range = range;
        this.delay = delay;
        this.type = type;
    }

    @Override
    public boolean shouldStart() {
        this.cachedTarget = findTarget();
        return this.cachedTarget != null;
    }

    @Override
    public void start() {
        final Point targetPosition = this.cachedTarget.getPosition();
        entityCreature.getNavigator().setPathTo(targetPosition);
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
                if (lookAt) {
                    entityCreature.lookAt(target);
                }
                if (!Cooldown.hasCooldown(time, lastHit, delay) && !entityCreature.isDead()) {
                    entityCreature.attack(target, true);
                    this.lastHit = time;
                }
                return;
            }

            if (SkyBlockRegion.getRegionOfEntity(target) == null
                    || SkyBlockRegion.getRegionOfEntity(target).getType() != type) {
                this.stop = true;
                end();
                return;
            }

            // Move toward the target entity
            Navigator navigator = entityCreature.getNavigator();
            final var pathPosition = navigator.getPathPosition();
            final var targetPosition = target.getPosition();
            if (pathPosition == null || !pathPosition.samePoint(targetPosition)) {
                if (this.cooldown.isReady(time)) {
                    this.cooldown.refreshLastUpdate(time);
                    navigator.setPathTo(targetPosition);
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
        entityCreature.getNavigator().setPathTo(null);
    }
}
