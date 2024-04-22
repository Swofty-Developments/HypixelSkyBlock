package net.swofty.types.generic.entity.mob;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.entity.mob.impl.RegionPopulator;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.loottable.LootAffector;
import net.swofty.types.generic.loottable.SkyBlockLootTable;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.StringUtility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        this.getAttribute(Attribute.MAX_HEALTH).setBaseValue(getBaseStatistics().getOverall(ItemStatistic.HEALTH).floatValue());
        this.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((float) ((getBaseStatistics().getOverall(ItemStatistic.SPEED).floatValue() / 1000) * 2.5));
        this.setHealth(getBaseStatistics().getOverall(ItemStatistic.HEALTH).floatValue());

        this.setCustomName(Component.text(
            "§8[§7Lv" + getLevel() + "§8] §c" + getDisplayName()
                + " §a" + Math.round(getHealth())
                + "§f/§a"
                + Math.round(getStatistics().getOverall(ItemStatistic.HEALTH))
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
    public abstract Double getCoins();
    public abstract List<GoalSelector> getGoalSelectors();
    public abstract List<TargetSelector> getTargetSelectors();
    public abstract ItemStatistics getBaseStatistics();
    public abstract @Nullable SkyBlockLootTable getLootTable();
    public abstract SkillCategories getSkillCategory();
    public abstract long damageCooldown();
    public abstract long getSkillXP();

    public ItemStatistics getStatistics() {
        ItemStatistics statistics = getBaseStatistics().clone();
        ItemStatistics toSubtract = ItemStatistics.builder()
            .withAdditive(ItemStatistic.HEALTH, (double) getHealth())
            .build();

        return statistics.sub(toSubtract);
    }

    @Override
    public boolean damage(@NotNull Damage damage) {
        boolean toReturn = super.damage(damage);

        setHasBeenDamaged(true);

        Entity sourcePoint = damage.getSource();
        if (sourcePoint != null) {
            takeKnockback(0.4f,
                    Math.sin(sourcePoint.getPosition().yaw() * Math.PI / 180),
                    -Math.cos(sourcePoint.getPosition().yaw() * Math.PI / 180));
        }

        this.setCustomName(Component.text(
                "§8[§7Lv" + getLevel() + "§8] §c" + getDisplayName()
                        + " §a" + Math.round(getHealth())
                        + "§f/§a"
                        + Math.round(this.getAttributeValue(Attribute.MAX_HEALTH))
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

        player.playSound(Sound.sound(Key.key("entity." + getEntityType().name().toLowerCase().replace("minecraft:", "") + ".death"), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());

        player.getSkills().setRaw(player, getSkillCategory(), player.getSkills().getRaw(getSkillCategory()) + getSkillXP());
        player.playSound(Sound.sound(Key.key("entity." + getEntityType().name().toLowerCase().replace("minecraft:", "") + ".death"), Sound.Source.PLAYER, 1f, 1f), Sound.Emitter.self());

        if (getLootTable() == null) return;
        if (getLastDamageSource() == null) return;
        if (getLastDamageSource().getAttacker() == null) return;
        if (getCoins() == null) return;

        Map<ItemType, SkyBlockLootTable.LootRecord> drops = getLootTable()
            .runChances(player, LootAffector.MAGIC_FIND, LootAffector.ENCHANTMENT_LUCK);

        for (ItemType itemType : drops.keySet()) {
            SkyBlockLootTable.LootRecord record = drops.get(itemType);

            if (SkyBlockLootTable.LootRecord.isNone(record)) continue;

            SkyBlockItem item = new SkyBlockItem(itemType, record.getAmount());
            DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(item, player);
            droppedItem.setInstance(getInstance(), getPosition().add(0, 0.5, 0));
        }

        DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
        coins.setValue(coins.getValue() + getCoins());
        player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                .ticksToLast(20)
                .display(StringUtility.commaify(getCoins()))
                .build(), StatisticDisplayReplacement.DisplayType.COINS);
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
