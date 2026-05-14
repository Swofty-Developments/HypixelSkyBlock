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

public class MobMagmaSlug extends SeaCreatureMob {

    public MobMagmaSlug() {
        super(EntityType.MAGMA_CUBE);
    }

    @Override
    public String getSeaCreatureId() {
        return "MAGMA_SLUG";
    }

    @Override
    public String getDisplayName() {
        return "Magma Slug";
    }

    @Override
    public Integer getLevel() {
        return 21;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new MeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK),
                new RandomStrollGoal(this, 12)
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return List.of(
                new LastEntityDamagerTarget(this, 14),
                new ClosestEntityTarget(this, 14, entity -> entity instanceof SkyBlockPlayer)
        );
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 850D)
                .withBase(ItemStatistic.DAMAGE, 170D)
                .withBase(ItemStatistic.SPEED, 50D)
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
        return new OtherLoot(450, 0, 12);
    }

    @Override
    public List<MobType> getMobTypes() {
        return List.of(MobType.MAGMATIC, MobType.CUBIC);
    }
}
