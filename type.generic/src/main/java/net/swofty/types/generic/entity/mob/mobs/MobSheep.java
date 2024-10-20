package net.swofty.types.generic.entity.mob.mobs;

import lombok.NonNull;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.loottable.SkyBlockLootTable;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

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
        return List.of(
                new RandomStrollGoal(this, 15)
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return new ArrayList<>();
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 100D)
                .withBase(ItemStatistic.SPEED, 70D)
                .build();
    }

    @Override
    public @Nullable SkyBlockLootTable getLootTable() {
        return new SkyBlockLootTable() {
            @Override
            public @NonNull List<LootRecord> getLootTable() {
                return List.of(
                        new LootRecord(ItemType.MUTTON, makeAmountBetween(1, 3), 80)
                );
            }

            @Override
            public @NonNull CalculationMode getCalculationMode() {
                return CalculationMode.CALCULATE_INDIVIDUAL;
            }
        };
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
    public long getSkillXP() {
        return 4;
    }
}
