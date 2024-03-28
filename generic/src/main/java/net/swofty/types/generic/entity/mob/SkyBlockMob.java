package net.swofty.types.generic.entity.mob;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.List;

public abstract class SkyBlockMob extends EntityCreature {
    @Getter
    private static List<SkyBlockMob> mobs = new ArrayList<>();

    public SkyBlockMob(EntityType entityType) {
        super(entityType);

        this.setCustomName(Component.text(
                "§8[§7Lv" + getLevel() + "§8] §c" + getDisplayName()
                        + " §a" + getHealth()
                        + "§f/§a"
                        + getStatistics().get(ItemStatistic.HEALTH)
        ));
        this.setCustomNameVisible(true);
        this.setHealth(getHealth());

        setAutoViewable(true);
        setAutoViewEntities(true);

        this.addAIGroup(getGoalSelectors(), getTargetSelectors());
    }

    @Override
    public void spawn() {
        super.spawn();
        mobs.add(this);
    }

    public abstract String getDisplayName();
    public abstract Integer getLevel();
    public abstract List<GoalSelector> getGoalSelectors();
    public abstract List<TargetSelector> getTargetSelectors();
    public abstract ItemStatistics getStatistics();
    public abstract List<MobDrop> getDrops();
    public abstract SkillCategories getSkillCategory();

    public record MobDrop(float chance, int min, int max, ItemType item) { }

    @Override
    public void kill() {
        super.kill();
        mobs.add(this);

        if (!(getLastDamageSource().getAttacker() instanceof SkyBlockPlayer)) return;
        SkyBlockPlayer player = (SkyBlockPlayer) getLastDamageSource().getAttacker();

        player.getSkills().set(player, getSkillCategory(), player.getSkills().get(getSkillCategory()) + 7);

        if (getDrops().isEmpty()) return;
        if (getLastDamageSource() == null) return;
        if (getLastDamageSource().getAttacker() == null) return;

        float random = (float) Math.random();

        for (MobDrop drop : getDrops()) {
            if (random <= drop.chance / 100) {
                int amount = (int) (Math.random() * (drop.max - drop.min) + drop.min);
                ItemType item = drop.item;

                DroppedItemEntityImpl droppedItemEntity = new DroppedItemEntityImpl(
                        new SkyBlockItem(item, amount), player
                );
                droppedItemEntity.setInstance(getInstance(), getPosition().add(
                        Math.random() * 0.5, 0.3, Math.random() * 0.5
                ));
            }
        }
    }

    public static void runRegionPopulators(Scheduler scheduler) {
        if (SkyBlockConst.isIslandServer()) return;

        scheduler.submitTask(() -> {
            if (SkyBlockGenericLoader.getLoadedPlayers().isEmpty()) return TaskSchedule.seconds(10);

            MobRegistry.getMobsToRegionPopulate().forEach(mobRegistry -> {
                RegionPopulator regionPopulator = (RegionPopulator) mobRegistry.getMobCache();

                regionPopulator.getPopulators().forEach(populator -> {
                    RegionType regionType = populator.regionType();
                    int minimumAmountToPopulate = populator.minimumAmountToPopulate();

                    int amountInRegion = 0;

                    for (SkyBlockMob mob : SkyBlockRegion.getMobsInRegion(regionType)) {
                        if (!MobRegistry.getFromMob(mob).equals(mobRegistry)) {
                            continue;
                        }

                        amountInRegion++;
                    }

                    if (amountInRegion < minimumAmountToPopulate) {
                        for (int i = 0; i < minimumAmountToPopulate - amountInRegion; i++)
                            RegionPopulator.populateRegion(mobRegistry, populator);
                    }
                });
            });

            return TaskSchedule.seconds(5);
        });
    }
}
