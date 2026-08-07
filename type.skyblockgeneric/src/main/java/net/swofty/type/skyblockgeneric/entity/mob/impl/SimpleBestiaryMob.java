package net.swofty.type.skyblockgeneric.entity.mob.impl;

import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.entity.mob.ai.ClosestEntityRegionTarget;
import net.swofty.type.skyblockgeneric.entity.mob.ai.VanillaMeleeAttackGoal;
import net.swofty.type.skyblockgeneric.entity.mob.ai.VanillaRandomStrollGoal;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleBestiaryMob extends BestiaryMob {
    protected final MobDefinition definition;

    protected SimpleBestiaryMob(MobDefinition definition) {
        this(definition, true);
    }

    protected SimpleBestiaryMob(MobDefinition definition, boolean initialize) {
        super(definition.entityType(), false);
        this.definition = definition;
        if (initialize) {
            initializeMob();
        }
    }

    @Override
    public String getDisplayName() {
        return definition.displayName();
    }

    @Override
    public Integer getLevel() {
        return definition.level();
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        List<GoalSelector> goals = new ArrayList<>();
        if (definition.hostile()) {
            goals.add(new VanillaMeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK));
        }
        goals.add(new VanillaRandomStrollGoal(this, 15));
        return goals;
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        if (!definition.hostile()) {
            return List.of();
        }

        List<TargetSelector> targets = new ArrayList<>(List.of(new LastEntityDamagerTarget(this, 16)));
        if (definition.targetsPlayers()) {
            targets.add(new ClosestEntityRegionTarget(this, 16,
                    entity -> entity instanceof SkyBlockPlayer,
                    definition.targetRegion() == null ? RegionType.PRIVATE_ISLAND : definition.targetRegion()));
        }
        return targets;
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return statistics(definition.health(), definition.damage(), definition.speed());
    }

    protected final ItemStatistics statistics(double health, double damage, double speed) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, health)
                .withBase(ItemStatistic.DAMAGE, damage)
                .withBase(ItemStatistic.SPEED, speed)
                .build();
    }

    @Override
    public @Nullable SkyBlockLootTable getLootTable() {
        return new SkyBlockLootTable() {
            @Override
            public @NotNull List<LootRecord> getLootTable() {
                return definition.drops().stream()
                        .map(drop -> new LootRecord(drop.itemType(), makeAmountBetween(drop.minimum(), drop.maximum()), drop.chancePercent()))
                        .toList();
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
        return new OtherLoot(definition.combatXp(), definition.coins(), definition.xpOrbs());
    }

    @Override
    public List<MobType> getMobTypes() {
        return definition.mobTypes();
    }

    @Override
    public int getMaxBestiaryTier() {
        return definition.maxBestiaryTier();
    }

    @Override
    public int getBestiaryBracket() {
        return definition.bestiaryBracket();
    }

    @Override
    public String getMobID() {
        return definition.mobId();
    }

    @Override
    public GUIMaterial getGuiMaterial() {
        return new GUIMaterial(definition.guiMaterial());
    }
}
