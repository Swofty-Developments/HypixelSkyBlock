package net.swofty.types.generic.entity.mob;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Setter
public abstract class SkyBlockMob extends EntityCreature {
    @Getter
    private static List<SkyBlockMob> mobs = new ArrayList<>();
    @Getter
    private long lastAttack = System.currentTimeMillis();
    @Getter
    private boolean hasBeenDamaged = false;

    public SkyBlockMob(EntityType entityType) {
        super(entityType);

        this.setCustomNameVisible(true);
        this.getAttribute(Attribute.MAX_HEALTH).setBaseValue(getBaseStatistics().get(ItemStatistic.HEALTH).floatValue());
        this.setHealth(getBaseStatistics().get(ItemStatistic.HEALTH).floatValue());

        this.setCustomName(Component.text(
                "§8[§7Lv" + getLevel() + "§8] §c" + getDisplayName()
                        + " §a" + Math.round(getHealth())
                        + "§f/§a"
                        + Math.round(getStatistics().get(ItemStatistic.HEALTH))
        ));

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
    public abstract ItemStatistics getBaseStatistics();
    public abstract List<MobDrop> getDrops();
    public abstract SkillCategories getSkillCategory();
    public abstract long damageCooldown();

    public record MobDrop(float chance, int min, int max, ItemType item) { }

    public ItemStatistics getStatistics() {
        ItemStatistics statistics = getBaseStatistics().clone();

        // Set health to current enemy health
        statistics.set(ItemStatistic.HEALTH, (double) getHealth());

        return statistics;
    }

    @Override
    public boolean damage(@NotNull Damage damage) {
        boolean toReturn = super.damage(damage);

        setHasBeenDamaged(true);

        Point sourcePoint = damage.getSourcePosition();
        if (sourcePoint != null) {
            double x = sourcePoint.x() - getPosition().x();
            double z = sourcePoint.z() - getPosition().z();
            double distance = Math.sqrt(x * x + z * z);
            double knockback = 2;
            setVelocity(new Vec(x / distance * knockback, 0.5, z / distance * knockback));
        }

        this.setCustomName(Component.text(
                "§8[§7Lv" + getLevel() + "§8] §c" + getDisplayName()
                        + " §a" + Math.round(getHealth())
                        + "§f/§a"
                        + Math.round(getStatistics().get(ItemStatistic.HEALTH))
        ));

        return toReturn;
    }

    @Override
    public void kill() {
        super.kill();
        mobs.remove(this);

        if (!(getLastDamageSource().getAttacker() instanceof SkyBlockPlayer)) return;
        SkyBlockPlayer player = (SkyBlockPlayer) getLastDamageSource().getAttacker();

        SkyBlockEvent.callSkyBlockEvent(new PlayerKilledSkyBlockMobEvent(player, this));

        player.getSkills().setRaw(player, getSkillCategory(), player.getSkills().getRaw(getSkillCategory()) + 7);

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
