package net.swofty.types.generic.entity.mob.mobs.island;

import lombok.NonNull;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.entity.mob.ai.MeleeAttackWithinRegionGoal;
import net.swofty.types.generic.entity.mob.ai.RandomRegionStrollGoal;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.loottable.OtherLoot;
import net.swofty.types.generic.loottable.SkyBlockLootTable;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategories;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MobZombie_2 extends MobZombie_1 {
    @Override
    public Integer getLevel() {
        return 2;
    }

    @Override
    public String getMobID() {
        return "2_zombie";
    }
}
