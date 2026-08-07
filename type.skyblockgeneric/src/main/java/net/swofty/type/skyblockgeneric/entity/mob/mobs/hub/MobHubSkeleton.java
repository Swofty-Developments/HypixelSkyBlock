package net.swofty.type.skyblockgeneric.entity.mob.mobs.hub;

import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.goal.RangedAttackGoal;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.type.skyblockgeneric.entity.mob.ai.VanillaRandomStrollGoal;
import net.swofty.type.skyblockgeneric.entity.mob.impl.SimpleBestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.impl.StandardMobDefinitions;

import java.util.List;

public class MobHubSkeleton extends SimpleBestiaryMob {
    public MobHubSkeleton() {
        super(StandardMobDefinitions.hubSkeleton());
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new RangedAttackGoal(this, 40, 16, 8, true, 1, 0.1, TimeUnit.SERVER_TICK),
                new VanillaRandomStrollGoal(this, 15));
    }
}
