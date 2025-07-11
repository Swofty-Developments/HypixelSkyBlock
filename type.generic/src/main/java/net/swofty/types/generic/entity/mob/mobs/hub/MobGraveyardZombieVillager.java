package net.swofty.types.generic.entity.mob.mobs.hub;

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
import net.swofty.types.generic.entity.mob.ai.ClosestEntityRegionTarget;
import net.swofty.types.generic.entity.mob.ai.MeleeAttackWithinRegionGoal;
import net.swofty.types.generic.entity.mob.ai.RandomRegionStrollGoal;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.loottable.OtherLoot;
import net.swofty.types.generic.loottable.SkyBlockLootTable;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class MobGraveyardZombieVillager extends BestiaryMob implements RegionPopulator {

    public MobGraveyardZombieVillager() {
        super(EntityType.ZOMBIE_VILLAGER);
    }

    @Override
    public String getDisplayName() {
        return "Zombie Villager";
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new MeleeAttackWithinRegionGoal(this,
                        1.6,
                        20,
                        TimeUnit.SERVER_TICK,
                        RegionType.GRAVEYARD), // Attack the target
                new RandomRegionStrollGoal(this, 15, RegionType.GRAVEYARD)  // Walk around
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return List.of(
                new LastEntityDamagerTarget(this, 16), // First target the last entity which attacked you
                new ClosestEntityRegionTarget(this,
                        16,
                        entity -> entity instanceof SkyBlockPlayer,
                        RegionType.GRAVEYARD) // If there is none, target the nearest player
        );
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 120D)
                .withBase(ItemStatistic.DAMAGE, 24D)
                .withBase(ItemStatistic.SPEED, 100D)
                .build();
    }

    @Override
    public @Nullable SkyBlockLootTable getLootTable() {
        return new SkyBlockLootTable() {
            @Override
            public @NonNull List<LootRecord> getLootTable() {
                return List.of(
                        new LootRecord(ItemType.ROTTEN_FLESH, makeAmountBetween(1, 3), 20)
                );
            }

            @Override
            public @NotNull CalculationMode getCalculationMode() {
                return CalculationMode.CALCULATE_INDIVIDUAL;
            }
        };
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
    public OtherLoot getOtherLoot() {
        return new OtherLoot(7, 1, 2);
    }

    @Override
    public int getMaxBestiaryTier() {
        return 15;
    }

    @Override
    public int getBestiaryBracket() {
        return 4;
    }

    @Override
    public String getMobID() {
        return "1_graveyard_zombie_villager";
    }

    @Override
    public List<Populator> getPopulators() {
        return Arrays.asList(
                new Populator(RegionType.GRAVEYARD, 5)
        );
    }
}
