package net.swofty.type.skyblockgeneric.entity.mob.seacreature;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobSeaGuardian extends SeaCreatureMob {

    public MobSeaGuardian() {
        super(EntityType.GUARDIAN);
    }

    @Override
    public String getSeaCreatureId() {
        return "SEA_GUARDIAN";
    }

    @Override
    public String getDisplayName() {
        return "Sea Guardian";
    }

    @Override
    public Integer getLevel() {
        return 8;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new MeleeAttackGoal(this, 2.0, 25, TimeUnit.SERVER_TICK),
                new RandomStrollGoal(this, 10)
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return List.of(
                new LastEntityDamagerTarget(this, 18),
                new ClosestEntityTarget(this, 18, entity -> entity instanceof SkyBlockPlayer)
        );
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 600D)
                .withBase(ItemStatistic.DAMAGE, 40D)
                .withBase(ItemStatistic.SPEED, 60D)
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
        return 400;
    }

    @Override
    public OtherLoot getOtherLoot() {
        return new OtherLoot(120, 0, 10);
    }

    @Override
    public List<MobType> getMobTypes() {
        return List.of(MobType.AQUATIC);
    }
}
