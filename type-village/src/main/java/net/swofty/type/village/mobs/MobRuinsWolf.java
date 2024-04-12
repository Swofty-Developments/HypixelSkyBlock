package net.swofty.type.village.mobs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.entity.mob.ai.ClosestEntityRegionTarget;
import net.swofty.types.generic.entity.mob.ai.MeleeAttackWithinRegionGoal;
import net.swofty.types.generic.entity.mob.ai.RandomRegionStrollGoal;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobRuinsWolf extends SkyBlockMob implements RegionPopulator {

    public MobRuinsWolf(EntityType entityType) {
        super(entityType);
    }

    @Override
    public String getDisplayName() {
        return "Wolf";
    }

    @Override
    public Integer getLevel() {
        return 15;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new MeleeAttackWithinRegionGoal(this,
                        1.6,
                        20,
                        TimeUnit.SERVER_TICK,
                        RegionType.RUINS), // Attack the target
                new RandomRegionStrollGoal(this, 15, RegionType.RUINS)  // Walk around
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return List.of(
                new LastEntityDamagerTarget(this, 16), // First target the last entity which attacked you
                new ClosestEntityRegionTarget(this,
                        16,
                        entity -> entity instanceof SkyBlockPlayer,
                        RegionType.RUINS) // If there is none, target the nearest player
        );
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 250D)
                .with(ItemStatistic.DAMAGE, 90D)
                .with(ItemStatistic.SPEED, 100D)
                .build();
    }

    @Override
    public List<MobDrop> getDrops() {
        return new ArrayList<>(List.of(
                new MobDrop(20f, 1, 3, ItemType.BONE)
        ));
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.COMBAT;
    }

    @Override
    public long damageCooldown() {
        return 500;
    }

    @Override
    public List<Populator> getPopulators() {
        return Arrays.asList(
                new Populator(RegionType.RUINS, 20)
        );
    }

    @Override
    public long getxp() {
        return 10;
    }
}
