package net.swofty.types.generic.entity.mob.ai;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Predicate;

public class ClosestEntityRegionTarget extends TargetSelector {

    private final double range;
    private final Predicate<Entity> targetPredicate;
    private final RegionType type;

    @SafeVarargs
    @Deprecated
    public ClosestEntityRegionTarget(@NotNull EntityCreature entityCreature, float range,
                                     RegionType type, @NotNull Class<? extends LivingEntity>... entitiesTarget) {
        this(entityCreature, range, ent -> {
            Class<? extends Entity> clazz = ent.getClass();
            for (Class<? extends LivingEntity> targetClass : entitiesTarget) {
                if (targetClass.isAssignableFrom(clazz)) {
                    return true;
                }
            }
            return false;
        }, type);
    }

    public ClosestEntityRegionTarget(@NotNull EntityCreature entityCreature, double range,
                                     @NotNull Predicate<Entity> targetPredicate, RegionType type) {
        super(entityCreature);
        this.range = range;
        this.targetPredicate = targetPredicate;
        this.type = type;
    }

    @Override
    public Entity findTarget() {
        Instance instance = entityCreature.getInstance();

        if (instance == null) {
            return null;
        }

        return instance.getNearbyEntities(entityCreature.getPosition(), range).stream()
                // Don't target our self and make sure entity is valid
                .filter(ent -> !entityCreature.equals(ent) && !ent.isRemoved())
                .filter(targetPredicate)
                .filter(ent -> {
                    SkyBlockRegion region = SkyBlockRegion.getRegionOfEntity(ent);
                    return region == null || region.getType() == type;
                })
                .min(Comparator.comparingDouble(e -> e.getDistanceSquared(entityCreature)))
                .orElse(null);

    }
}