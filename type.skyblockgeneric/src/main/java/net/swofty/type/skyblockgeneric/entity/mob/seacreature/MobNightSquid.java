package net.swofty.type.skyblockgeneric.entity.mob.seacreature;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobNightSquid extends SeaCreatureMob {

    public MobNightSquid() {
        super(EntityType.GLOW_SQUID);
    }

    @Override
    public String getSeaCreatureId() {
        return "NIGHT_SQUID";
    }

    @Override
    public String getDisplayName() {
        return "Night Squid";
    }

    @Override
    public Integer getLevel() {
        return 6;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(new RandomStrollGoal(this, 6));
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return List.of();
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 400D)
                .withBase(ItemStatistic.DAMAGE, 15D)
                .withBase(ItemStatistic.SPEED, 30D)
                .build();
    }

    @Override
    public @Nullable SkyBlockLootTable getLootTable() {
        return null;
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.FISHING;
    }

    @Override
    public long damageCooldown() {
        return 500;
    }

    @Override
    public OtherLoot getOtherLoot() {
        return new OtherLoot(80, 0, 8);
    }

    @Override
    public List<MobType> getMobTypes() {
        return List.of(MobType.AQUATIC);
    }
}
