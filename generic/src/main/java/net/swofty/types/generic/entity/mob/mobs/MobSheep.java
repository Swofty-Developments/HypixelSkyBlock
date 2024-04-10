package net.swofty.types.generic.entity.mob.mobs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public class MobSheep extends SkyBlockMob {
    public MobSheep(EntityType entityType) {
        super(entityType);
    }

    @Override
    public String getDisplayName() {
        return "Sheep";
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return new ArrayList<>();
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return new ArrayList<>();
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 100D)
                .build();
    }

    @Override
    public List<MobDrop> getDrops() {
        return new ArrayList<>();
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.FARMING;
    }

    @Override
    public long damageCooldown() {
        return 200;
    }

    @Override
    public long getxp() {
        return 0;
    }
}
