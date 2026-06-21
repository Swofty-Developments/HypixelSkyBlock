package net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SlayerBossBehaviour {
    public static List<GoalSelector> goals(SkyBlockMob self) {
        return List.of(
            new MeleeAttackGoal(self, 1.8, 16, TimeUnit.SERVER_TICK),
            new RandomStrollGoal(self, 12)
        );
    }

    public static List<TargetSelector> targets(SkyBlockMob self) {
        return List.of(
            new LastEntityDamagerTarget(self, 24),
            new ClosestEntityTarget(self, 24, entity -> entity instanceof SkyBlockPlayer)
        );
    }
}
