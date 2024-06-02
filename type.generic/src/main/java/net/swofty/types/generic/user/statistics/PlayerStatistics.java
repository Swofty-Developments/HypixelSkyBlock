package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.enchantment.abstr.EventBasedEnchant;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.events.RegenerationValueUpdateEvent;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeRuneInfusedWith;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.HotPotatoable;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.levels.unlocks.SkyBlockLevelStatisticUnlock;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.util.*;

public class PlayerStatistics {
    private static final Map<Player, BossBar> barCache = new HashMap<>();

    private final SkyBlockPlayer player;
    @Setter
    @Getter
    private double healthRegenerationPercentBonus;
    @Setter
    @Getter
    private double manaRegenerationPercentBonus;
    private ItemStatistics accessoryStatistics = ItemStatistics.builder().build();
    private final List<TemporaryStatistic> temporaryStatistics = Collections.synchronizedList(new ArrayList<>());
    private final List<TemporaryConditionalStatistic> temporaryConditionalStatistics = Collections.synchronizedList(new ArrayList<>());

    public PlayerStatistics(SkyBlockPlayer player) {
        this.player = player;
    }

    public @Nullable SkyBlockItem getItemWithRune(ItemType runeType) {
        List<SkyBlockItem> piecesToCheck = getPossibleRuneItems();

        for (SkyBlockItem item : piecesToCheck) {
            if (item == null) continue;
            ItemAttributeRuneInfusedWith.RuneData runeData = item.getAttributeHandler().getRuneData();
            if (!runeData.hasRune()) continue;
            if (runeData.getRuneType() == runeType) continue;

            return item;
        }

        return null;
    }

    private List<SkyBlockItem> getPossibleRuneItems() {
        ArrayList<SkyBlockItem> piecesToCheck = new ArrayList<>();
        PlayerItemOrigin.OriginCache cache = PlayerItemOrigin.getFromCache(player.getUuid());

        piecesToCheck.add(cache.get(PlayerItemOrigin.MAIN_HAND));
        piecesToCheck.add(cache.get(PlayerItemOrigin.HELMET));
        piecesToCheck.add(cache.get(PlayerItemOrigin.CHESTPLATE));
        piecesToCheck.add(cache.get(PlayerItemOrigin.LEGGINGS));
        piecesToCheck.add(cache.get(PlayerItemOrigin.BOOTS));

        return piecesToCheck;
    }

    public ItemStatistics allArmorStatistics(SkyBlockPlayer causer, LivingEntity enemy) {
        PlayerItemOrigin.OriginCache cache = PlayerItemOrigin.getFromCache(player.getUuid());
        ArrayList<SkyBlockItem> armorPieces = new ArrayList<>();

        armorPieces.add(cache.get(PlayerItemOrigin.HELMET));
        armorPieces.add(cache.get(PlayerItemOrigin.CHESTPLATE));
        armorPieces.add(cache.get(PlayerItemOrigin.LEGGINGS));
        armorPieces.add(cache.get(PlayerItemOrigin.BOOTS));

        ItemStatistics total = ItemStatistics.builder().build();
        for (SkyBlockItem item : armorPieces) {
            if (item.getGenericInstance() != null)
                if (item.getGenericInstance() instanceof ConstantStatistics)
                    continue;

            total = ItemStatistics.add(total, ItemStatistics.add(item.getAttributeHandler().getStatistics(), calculateExtraItemStatisticsToAdd(
                    item,
                    causer,
                    enemy
            )));
        }
        if (player.getArmorSet() != null) {
            ArmorSetRegistry armorSetRegistry = player.getArmorSet();
            try {
                Constructor<? extends ArmorSet> constructor = armorSetRegistry.getClazz().getConstructor();
                ArmorSet armorSet = constructor.newInstance();
                total = ItemStatistics.add(total, armorSet.getStatistics());
            } catch (Exception _) {
            }
        }
        return total;
    }

    // Returns a map of Level : Amount of that level
    public @NonNull Map<Integer, Integer> getAllOfEnchants(EnchantmentType enchantmentType,
                                                           boolean includeAccessories) {
        List<SkyBlockItem> toCheck = new ArrayList<>();
        PlayerItemOrigin.OriginCache cache = PlayerItemOrigin.getFromCache(player.getUuid());

        toCheck.add(cache.get(PlayerItemOrigin.MAIN_HAND));
        toCheck.add(cache.get(PlayerItemOrigin.HELMET));
        toCheck.add(cache.get(PlayerItemOrigin.CHESTPLATE));
        toCheck.add(cache.get(PlayerItemOrigin.LEGGINGS));
        toCheck.add(cache.get(PlayerItemOrigin.BOOTS));

        if (includeAccessories) {
            toCheck.addAll(player.getAccessoryBag().getAllAccessories());
        }

        Map<Integer, Integer> enchantLevels = new HashMap<>();

        for (SkyBlockItem item : toCheck) {
            if (item == null) continue;

            for (SkyBlockEnchantment enchantment : item.getAttributeHandler().getEnchantments().toList()) {
                if (enchantment.type() != enchantmentType) continue;

                int level = enchantment.level();
                enchantLevels.merge(level, 1, Integer::sum);
            }
        }

        return enchantLevels;
    }

    public ItemStatistics mainHandStatistics(SkyBlockPlayer causer, LivingEntity enemy) {
        SkyBlockItem item = PlayerItemOrigin.getFromCache(player.getUuid()).get(PlayerItemOrigin.MAIN_HAND);

        if (item.getGenericInstance() != null) {
            if (item.getGenericInstance() instanceof ConstantStatistics)
                return ItemStatistics.empty();
            if (item.getGenericInstance() instanceof StandardItem standardItem)
                if (standardItem.getStandardItemType().isArmor())
                    return ItemStatistics.empty();
        }

        return ItemStatistics.add(item.getAttributeHandler().getStatistics(),
                calculateExtraItemStatisticsToAdd(
                        item,
                        causer,
                        enemy
                ));
    }

    public ItemStatistics petStatistics() {
        SkyBlockItem pet = player.getPetData().getEnabledPet();
        if (pet == null) return ItemStatistics.empty();
        ItemStatistics baseStatistics = ((Pet) pet.getGenericInstance()).getBaseStatistics();
        ItemStatistics perLevelStatistics = ((Pet) pet.getGenericInstance()).getPerLevelStatistics(
                pet.getAttributeHandler().getRarity()
        );
        int level = pet.getAttributeHandler().getPetData().getAsLevel(pet.getAttributeHandler().getRarity());
        return ItemStatistics.add(baseStatistics, ItemStatistics.multiply(perLevelStatistics, level));
    }

    public ItemStatistics spareStatistics() {
        ItemStatistics spare = ItemStatistics.builder().build();

        int fairySouls = player.getFairySouls().getExchangedFairySouls().size();
        spare = ItemStatistics.add(spare, ItemStatistics.builder().withBase(ItemStatistic.HEALTH, (double) (fairySouls * 2)).build());

        DatapointSkills.PlayerSkills skills = player.getSkills();
        spare = ItemStatistics.add(spare, skills.getSkillStatistics());

        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();
        for (int i = 0; i < experience.getLevel().asInt(); i++) {
            List<SkyBlockLevelStatisticUnlock> unlocks = experience.getLevel().getStatisticUnlocks();
            for (SkyBlockLevelStatisticUnlock unlock : unlocks) {
                spare = ItemStatistics.add(spare, unlock.getStatistics());
            }
        }

        return spare;
    }

    public long getInvulnerabilityTime() {
        double bonusAttackSpeed = allStatistics().getOverall(ItemStatistic.BONUS_ATTACK_SPEED);
        return (long) (10 / (1 + (bonusAttackSpeed / 100)));
    }


    public ItemStatistics allStatistics() {
        return allStatistics(null, null);
    }

    public ItemStatistics allStatistics(SkyBlockPlayer causer, LivingEntity enemy) {
        ItemStatistics total = ItemStatistics.builder().build();
        total = ItemStatistics.add(total, allArmorStatistics(causer, enemy));
        total = ItemStatistics.add(total, mainHandStatistics(causer, enemy));
        total = ItemStatistics.add(total, spareStatistics());
        total = ItemStatistics.add(total, getTemporaryStatistics());
        total = ItemStatistics.add(total, petStatistics());
        total = ItemStatistics.add(total, accessoryStatistics);
        total = ItemStatistics.add(total, ItemStatistic.getOfAllBaseValues());

        return total;
    }

    public void updateAccessoryStatistics() {
        List<ItemType> usedAccessories = new ArrayList<>();
        ItemStatistics total = ItemStatistics.builder().build();
        for (ItemStack itemStack : player.getInventory().getItemStacks()) {
            if (SkyBlockItem.isSkyBlockItem(itemStack)) {
                SkyBlockItem item = new SkyBlockItem(itemStack);
                if (item.getGenericInstance() == null) continue;
                if (!(item.getGenericInstance() instanceof ConstantStatistics)) continue;
                if (usedAccessories.contains(item.getAttributeHandler().getItemTypeAsType())) continue;

                usedAccessories.add(item.getAttributeHandler().getItemTypeAsType());
                total = ItemStatistics.add(total, ItemStatistics.add(item.getAttributeHandler().getStatistics(),
                        calculateExtraItemStatisticsToAdd(
                            item,
                            null,
                            null
                        )));
            }
        }
        for (SkyBlockItem item : player.getAccessoryBag().getAllAccessories()) {
            if (item == null) continue;
            if (item.getGenericInstance() == null) continue;
            if (!(item.getGenericInstance() instanceof ConstantStatistics)) continue;
            if (usedAccessories.contains(item.getAttributeHandler().getItemTypeAsType())) continue;

            usedAccessories.add(item.getAttributeHandler().getItemTypeAsType());
            total = ItemStatistics.add(total, ItemStatistics.add(item.getAttributeHandler().getStatistics(),
                    calculateExtraItemStatisticsToAdd(
                        item,
                        null,
                        null
            )));
        }
        accessoryStatistics = total;
    }

    private ItemStatistics calculateExtraItemStatisticsToAdd(SkyBlockItem item,
                                                        SkyBlockPlayer causer, LivingEntity enemy) {
        ItemStatistics statistics = ItemStatistics.builder().build();
        statistics = getReforgeStatistics(item, statistics);
        statistics = getGemstoneStatistics(item, statistics);
        statistics = getEnchantStatistics(item, statistics, causer, enemy);
        statistics = getHotPotatoBookStatistics(item, statistics);

        return statistics;
    }

    private ItemStatistics getReforgeStatistics(SkyBlockItem item, ItemStatistics statistics) {
        if (item.getAttributeHandler().getReforge() != null) {
            statistics = item.getAttributeHandler().getReforge().getAfterCalculation(statistics,
                    item.getAttributeHandler().getRarity().ordinal() + 1);
        }
        return statistics;
    }

    private ItemStatistics getHotPotatoBookStatistics(SkyBlockItem item, ItemStatistics statistics) {
        ItemAttributeHotPotatoBookData.HotPotatoBookData hotPotatoBookData = item.getAttributeHandler().getHotPotatoBookData();
        if (hotPotatoBookData.hasPotatoBook()) {
            ItemStatistics.Builder toAdd = ItemStatistics.builder();
            HotPotatoable.PotatoType potatoType = hotPotatoBookData.getPotatoType();

            potatoType.stats.forEach(toAdd::withBase);
            statistics = ItemStatistics.add(statistics, toAdd.build());
        }
        return statistics;
    }

    private ItemStatistics getEnchantStatistics(SkyBlockItem item, ItemStatistics statistics,
                                                SkyBlockPlayer causer, LivingEntity enemy) {
        for (SkyBlockEnchantment enchantment : item.getAttributeHandler().getEnchantments().toList()) {
            ItemStatistics enchantmentStatistics = enchantment.type().getEnch().getStatistics(enchantment.level()).clone();
            statistics = ItemStatistics.add(statistics, enchantmentStatistics);

            if (causer != null && enemy != null) {
                if (enchantment.type().getEnch() instanceof EventBasedEnchant eventBasedStatistic) {
                    statistics = ItemStatistics.add(statistics, eventBasedStatistic.getStatisticsOnDamage(
                            causer,
                            enemy,
                            enchantment.level()
                    ));
                }
            }
        }
        return statistics;
    }

    private ItemStatistics getTemporaryStatistics() {
        ItemStatistics statistics = ItemStatistics.builder().build();

        synchronized (temporaryStatistics) {
            temporaryStatistics.removeIf(temporaryStatistic -> temporaryStatistic.getExpiration() < System.currentTimeMillis());
            for (TemporaryStatistic temporaryStatistic : temporaryStatistics) {
                statistics = ItemStatistics.add(statistics, temporaryStatistic.getStatistics());
            }

            temporaryConditionalStatistics.removeIf(temporaryStatistic -> !temporaryStatistic.getExpiry().apply(player));
            for (TemporaryConditionalStatistic temporaryStatistic : temporaryConditionalStatistics) {
                statistics = ItemStatistics.add(statistics, temporaryStatistic.getStatistics().apply(player));
            }

            return statistics;
        }
    }

    private ItemStatistics getGemstoneStatistics(SkyBlockItem item, ItemStatistics statistics) {
        for (ItemStatistic statistic : ItemStatistic.values()) {
            int extra = Gemstone.getExtraStatisticFromGemstone(statistic, item);
            if (extra != 0) {
                statistics = statistics.addBase(statistic, (double) extra);
            }
        }
        return statistics;
    }

    public Map.Entry<Double, Boolean> runPrimaryDamageFormula(ItemStatistics enemyStatistics,
                                                              SkyBlockPlayer causer, LivingEntity enemy) {
        ItemStatistics all = allStatistics(causer, enemy);
        return runPrimaryDamageFormula(all, enemyStatistics);
    }

    public static Map.Entry<Double, Boolean> runPrimaryDamageFormula(ItemStatistics originStatistics, ItemStatistics enemyStatistics) {
        boolean isCrit = false;
        double critChance = originStatistics.getBase(ItemStatistic.CRIT_CHANCE);
        if (Math.random() <= (critChance / 100))
            isCrit = true;

        double baseDamage = originStatistics.getOverall(ItemStatistic.DAMAGE);
        double strength = originStatistics.getOverall(ItemStatistic.STRENGTH);
        double critDamage = originStatistics.getOverall(ItemStatistic.CRIT_DAMAGE);

        double strengthDamage = (1 + (strength / 100));
        double criticalDamage = isCrit ? 1 + (critDamage / 100) : 1;

        double damage = baseDamage * strengthDamage * criticalDamage;
        if (enemyStatistics.getOverall(ItemStatistic.DEFENSE) > 0)
            damage = damage * (1 - (enemyStatistics.getOverall(ItemStatistic.DEFENSE) / (enemyStatistics.getOverall(ItemStatistic.DEFENSE) + 100)));
        return new AbstractMap.SimpleEntry<>(damage, isCrit);
    }

    public void boostManaRegeneration(double percent, int ticks) {
        manaRegenerationPercentBonus += percent;

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            manaRegenerationPercentBonus -= percent;
        }, TaskSchedule.tick(ticks), TaskSchedule.stop());
    }

    public void boostStatistic(TemporaryStatistic temporaryStatistic) {
        temporaryStatistics.add(temporaryStatistic);
    }

    public void boostStatistic(TemporaryConditionalStatistic temporaryStatistic) {
        temporaryConditionalStatistics.add(temporaryStatistic);
    }

    public void boostHealthRegeneration(double percent, int ticks) {
        healthRegenerationPercentBonus += percent;

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            healthRegenerationPercentBonus -= percent;
        }, TaskSchedule.tick(ticks), TaskSchedule.stop());
    }

    public static void run() {
        barLoop();
        healthLoop();
        manaLoop();
        missionLoop();
        statisticsLoop();
        speedLoop();
    }

    public static void barLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                float absorption = player.getAdditionalHearts();
                String heartsColour = absorption > 0.0f ? "§6" : "§c";

                String healthText = heartsColour + Math.round(player.getHealth() + absorption) + "/" +
                        Math.round(player.getMaxHealth()) + "❤";
                String defenseText = player.getDefense() == 0 ? "" : "§a" + Math.round(player.getDefense()) + "❈ Defense";
                String manaText = "§b" + Math.round(player.getMana()) + "/" + Math.round(player.getMaxMana()) + "✎ Mana";

                if (player.getManaDisplayReplacement() != null) {
                    manaText = player.getManaDisplayReplacement().getDisplay();
                }

                if (player.getDefenseDisplayReplacement() != null) {
                    defenseText = player.getDefenseDisplayReplacement().getDisplay();
                }

                String toSend = healthText + "     " + defenseText + "     " + manaText;

                player.sendActionBar(Component.text(toSend));
            });
            return TaskSchedule.tick(4);
        }, ExecutionType.ASYNC);
    }

    public static void statisticsLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                Thread.startVirtualThread(() -> {
                    player.getStatistics().updateAccessoryStatistics();
                });
            });
            return TaskSchedule.seconds(2);
        });
    }

    public static void speedLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                Thread.startVirtualThread(() -> {
                    double speed = player.getStatistics().allStatistics().getOverall(ItemStatistic.SPEED);
                    player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue((float) (speed / 1000));
                });
            });
            return TaskSchedule.tick(4);
        });
    }

    public static void healthLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                if (player.getHealth() > player.getMaxHealth())
                    player.setHealth(player.getMaxHealth());

                if (player.getHealth() < player.getMaxHealth()) {
                    float healthToIncreaseBy = (float) (1.5 + ((int) player.getMaxHealth() * 0.01) +
                            ((1.5 + ((int) player.getMaxHealth() * 0.01)) * player.getStatistics().getHealthRegenerationPercentBonus()));

                    RegenerationValueUpdateEvent event = new RegenerationValueUpdateEvent(player, healthToIncreaseBy);
                    SkyBlockValueEvent.callValueUpdateEvent(event);
                    healthToIncreaseBy = (float) event.getValue();

                    player.setHealth(Math.min(player.getMaxHealth(), player.getHealth() + healthToIncreaseBy));
                }
            });
            return TaskSchedule.tick(30);
        }, ExecutionType.ASYNC);
    }

    public static void missionLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                if (player.getRegion() == null) return;
                RegionType region = player.getRegion().getType();
                List<MissionData.ActiveMission> activeMissions = player.getMissionData().getActiveMissions(region);

                if (activeMissions.isEmpty()) {
                    if (barCache.containsKey(player)) {
                        BossBar oldBar = barCache.get(player);
                        player.hideBossBar(oldBar);
                        barCache.remove(player);
                    }
                    return;
                }
                MissionData.ActiveMission activeMission = activeMissions.getFirst();
                BossBar bar;

                if (activeMission.isProgress()) {
                    int maxProgress = ((SkyBlockProgressMission) MissionData.getMissionClass(activeMission)).getMaxProgress();
                    float progress = (float) activeMission.getMissionProgress() / maxProgress;

                    bar = BossBar.bossBar(
                            Component.text(
                                    "Objective: §e" + MissionData.getMissionClass(activeMission).getName()
                                            + "  §7(§e" + activeMission.getMissionProgress() + "§7/§a" + maxProgress + "§7)"),
                            progress,
                            BossBar.Color.YELLOW,
                            BossBar.Overlay.PROGRESS);
                } else {
                    bar = BossBar.bossBar(
                            Component.text("Objective: §e" + MissionData.getMissionClass(activeMission).getName()),
                            1f,
                            BossBar.Color.YELLOW,
                            BossBar.Overlay.NOTCHED_6);
                }

                if (barCache.containsKey(player)) {
                    BossBar oldBar = barCache.get(player);
                    if (oldBar.equals(bar)) return;
                    player.hideBossBar(oldBar);
                }
                player.showBossBar(bar);
                barCache.put(player, bar);
            });
            return TaskSchedule.tick(30);
        }, ExecutionType.ASYNC);
    }

    public static void manaLoop() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
                if (player.getMana() > player.getMaxMana())
                    player.setMana(player.getMaxMana());
                if (player.getMana() <= player.getMaxMana()) {
                    float manaPool = player.getMaxMana();
                    player.setMana(Math.min(manaPool, Math.min(manaPool, player.getMana() + (manaPool / 50) +
                            (int) ((manaPool / 50) * player.getStatistics().getManaRegenerationPercentBonus()))));
                }
            });
            return TaskSchedule.seconds(1);
        }, ExecutionType.ASYNC);
    }
}


