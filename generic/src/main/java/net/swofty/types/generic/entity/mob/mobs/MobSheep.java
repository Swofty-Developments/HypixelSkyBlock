package net.swofty.types.generic.entity.mob.mobs;

import lombok.NonNull;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.loottable.SkyBlockLootTable;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.NotNull;
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
        return new ArrayList<>();
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return new ArrayList<>();
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.HEALTH, 120D)
                .withAdditive(ItemStatistic.SPEED, 70D)
                .build();
    }

    @Override
    public @Nullable SkyBlockLootTable getLootTable() {
        return new SkyBlockLootTable() {
            @Override
            public @NonNull List<LootRecord> getLootTable() {
                return List.of(
                        new LootRecord(ItemType.MUTTON, 1, 50),
                        new LootRecord(ItemType.WOOL, 1, 50));
            }

            @Override
            public @NotNull CalculationMode getCalculationMode() {
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
    public Double getCoins() {
        return 1D;
    }

    @Override
    public long getSkillXP() {
        return 3;
    }
}
