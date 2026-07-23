package net.swofty.type.skyblockgeneric.entity.mob.mobs.seacreature;

import java.util.List;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.entity.mob.ai.VanillaMeleeAttackGoal;
import net.swofty.type.skyblockgeneric.entity.mob.ai.VanillaRandomStrollGoal;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Strategy describing how a sea creature moves and selects targets. The
 * sealed hierarchy is exhaustive — pattern-match on it elsewhere if you
 * need to introspect (e.g. catalog GUIs that render "aggressive" vs
 * "passive" differently).
 */
public sealed interface SeaCreatureBehaviour
        permits SeaCreatureBehaviour.Passive,
                SeaCreatureBehaviour.Aggressive,
                SeaCreatureBehaviour.Custom {

    List<GoalSelector> goals(SkyBlockMob self);

    List<TargetSelector> targets(SkyBlockMob self);

    static Passive passive(int strollRange) {
        return new Passive(strollRange);
    }

    static Aggressive aggressive(double speed, int attackCooldownTicks, int aggroRange) {
        return new Aggressive(speed, attackCooldownTicks, aggroRange);
    }

    static Custom of(List<GoalSelector> goals, List<TargetSelector> targets) {
        return new Custom(goals, targets);
    }

    record Passive(int strollRange) implements SeaCreatureBehaviour {
        @Override
        public List<GoalSelector> goals(SkyBlockMob self) {
            return List.of(new VanillaRandomStrollGoal(self, strollRange));
        }

        @Override
        public List<TargetSelector> targets(SkyBlockMob self) {
            return List.of();
        }
    }

    record Aggressive(double speed, int attackCooldownTicks, int aggroRange) implements SeaCreatureBehaviour {
        @Override
        public List<GoalSelector> goals(SkyBlockMob self) {
            return List.of(
                    new VanillaMeleeAttackGoal(self, speed, attackCooldownTicks, TimeUnit.SERVER_TICK),
                    new VanillaRandomStrollGoal(self, Math.max(8, aggroRange / 2))
            );
        }

        @Override
        public List<TargetSelector> targets(SkyBlockMob self) {
            return List.of(
                    new LastEntityDamagerTarget(self, aggroRange),
                    new ClosestEntityTarget(self, aggroRange, entity -> entity instanceof SkyBlockPlayer)
            );
        }
    }

    record Custom(List<GoalSelector> customGoals, List<TargetSelector> customTargets) implements SeaCreatureBehaviour {
        @Override
        public List<GoalSelector> goals(SkyBlockMob self) {
            return customGoals;
        }

        @Override
        public List<TargetSelector> targets(SkyBlockMob self) {
            return customTargets;
        }
    }
}
