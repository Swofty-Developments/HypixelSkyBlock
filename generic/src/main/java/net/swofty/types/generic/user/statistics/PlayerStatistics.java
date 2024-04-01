package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointIntegerList;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.events.RegenerationValueUpdateEvent;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.ConstantStatistics;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.levels.unlocks.SkyBlockLevelStatisticUnlock;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.SkyBlockProgressMission;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

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

    public PlayerStatistics(SkyBlockPlayer player) {
        this.player = player;
    }

    public ItemStatistics allArmorStatistics() {
        ArrayList<SkyBlockItem> armorPieces = new ArrayList<>();

        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chestplate = player.getInventory().getChestplate();
        ItemStack leggings = player.getInventory().getLeggings();
        ItemStack boots = player.getInventory().getBoots();
        if (SkyBlockItem.isSkyBlockItem(helmet)) armorPieces.add(new SkyBlockItem(helmet));
        if (SkyBlockItem.isSkyBlockItem(chestplate)) armorPieces.add(new SkyBlockItem(chestplate));
        if (SkyBlockItem.isSkyBlockItem(leggings)) armorPieces.add(new SkyBlockItem(leggings));
        if (SkyBlockItem.isSkyBlockItem(boots)) armorPieces.add(new SkyBlockItem(boots));

        ItemStatistics total = ItemStatistics.builder().build();
        for (SkyBlockItem item : armorPieces) {
            if (item.getGenericInstance() != null)
                if (item.getGenericInstance() instanceof ConstantStatistics)
                    continue;

            total = total.add(calculateExtraItemStatistics(item, item.getAttributeHandler().getStatistics()));
        }
        return total;
    }

    public ItemStatistics mainHandStatistics() {
        ItemStack mainhand = player.getInventory().getItemInMainHand();
        if (!SkyBlockItem.isSkyBlockItem(mainhand)) return ItemStatistics.EMPTY;

        SkyBlockItem item = new SkyBlockItem(mainhand);

        if (item.getGenericInstance() != null)
            if (item.getGenericInstance() instanceof ConstantStatistics)
                return ItemStatistics.EMPTY;

        ItemStatistics statistics = item.getAttributeHandler().getStatistics();
        statistics = calculateExtraItemStatistics(item, statistics);

        return statistics;
    }

    public ItemStatistics petStatistics() {
        SkyBlockItem pet = player.getPetData().getEnabledPet();
        if (pet == null) return ItemStatistics.EMPTY;

        ItemStatistics perLevelStatistics = ((Pet) pet.getGenericInstance()).getPerLevelStatistics(
                pet.getAttributeHandler().getRarity()
        );
        int level = pet.getAttributeHandler().getPetData().getAsLevel(pet.getAttributeHandler().getRarity());

        ItemStatistics statistics = ItemStatistics.builder().build();
        for (ItemStatistic statistic : ItemStatistic.values()) {
            statistics = statistics.add(ItemStatistics.builder()
                    .with(statistic, perLevelStatistics.get(statistic) * level)
                    .build());
        }

        return statistics;
    }

    public ItemStatistics spareStatistics() {
        ItemStatistics spare = ItemStatistics.builder().build();

        int fairySouls = player.getFairySouls().getExchangedFairySouls().size();
        spare = spare.add(ItemStatistics.builder().with(ItemStatistic.HEALTH, (double) (fairySouls * 2)).build());

        DatapointSkills.PlayerSkills skills = player.getSkills();
        for (Map.Entry<ItemStatistic, Double> entry : skills.getSkillStatistics().entrySet()) {
            ItemStatistic statistic = entry.getKey();
            Double value = entry.getValue();
            spare = spare.add(ItemStatistics.builder().with(statistic, value).build());
        }

        DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();
        for (int i = 0; i < experience.getLevel().asInt(); i++) {
            List<SkyBlockLevelStatisticUnlock> unlocks = experience.getLevel().getStatisticUnlocks();
            for (SkyBlockLevelStatisticUnlock unlock : unlocks) {
                spare = spare.add(unlock.getStatistics());
            }
        }

        return spare;
    }

    public ItemStatistics allStatistics() {
        ItemStatistics total = ItemStatistics.builder().build();
        total = total.add(allArmorStatistics());
        total = total.add(mainHandStatistics());
        total = total.add(spareStatistics());
        total = total.add(petStatistics());
        total = total.add(accessoryStatistics);

        ItemStatistics baseStats = ItemStatistics.builder()
                .with(ItemStatistic.HEALTH, 100D)
                .with(ItemStatistic.SPEED, 100D)
                .build();

        total = total.add(baseStats);

        return total;
    }

    public void updateAccessoryStatistics() {
        ItemStatistics total = ItemStatistics.builder().build();
        for (ItemStack itemStack : player.getInventory().getItemStacks()) {
            if (SkyBlockItem.isSkyBlockItem(itemStack)) {
                SkyBlockItem item = new SkyBlockItem(itemStack);
                if (item.getGenericInstance() == null) continue;
                if (!(item.getGenericInstance() instanceof ConstantStatistics)) continue;

                total = total.add(calculateExtraItemStatistics(item, item.getAttributeHandler().getStatistics()));
            }
        }
        for (SkyBlockItem item : player.getAccessoryBag().getAllAccessories()) {
            if (item == null) continue;
            if (item.getGenericInstance() == null) continue;
            if (!(item.getGenericInstance() instanceof ConstantStatistics)) continue;

            total = total.add(calculateExtraItemStatistics(item, item.getAttributeHandler().getStatistics()));
        }
        accessoryStatistics = total;
    }

    private ItemStatistics calculateExtraItemStatistics(SkyBlockItem item, ItemStatistics statistics) {
        statistics = getReforgeStatistics(item, statistics);
        statistics = getGemstoneStatistics(item, statistics);
        statistics = getEnchantStatistics(item, statistics);

        return statistics;
    }

    private ItemStatistics getReforgeStatistics(SkyBlockItem item, ItemStatistics statistics) {
        if (item.getAttributeHandler().getReforge() != null) {
            ItemStatistics.ItemStatisticsBuilder builder = ItemStatistics.builder();
            for (ItemStatistic statistic : item.getAttributeHandler().getReforge().getStatistics()) {
                builder.with(statistic, item.getAttributeHandler().getReforge().getBonusCalculation(
                        statistic,
                        item.getAttributeHandler().getRarity().ordinal()));
            }
            statistics = statistics.add(builder.build());
        }
        return statistics;
    }

    private ItemStatistics getEnchantStatistics(SkyBlockItem item, ItemStatistics statistics) {
        for (SkyBlockEnchantment enchantment : item.getAttributeHandler().getEnchantments().toList()) {
            ItemStatistics enchantmentStatistics = enchantment.type().getEnch().getStatistics(enchantment.level());
            statistics = statistics.add(enchantmentStatistics);
        }
        return statistics;
    }

    private ItemStatistics getGemstoneStatistics(SkyBlockItem item, ItemStatistics statistics) {
        for (ItemStatistic statistic : ItemStatistic.values()) {
            int extra = Gemstone.getExtraStatisticFromGemstone(statistic, item);
            if (extra != 0) {
                statistics = statistics.add(ItemStatistics.builder().with(statistic, (double) extra).build());
            }
        }
        return statistics;
    }

    public Map.Entry<Double, Boolean> runPrimaryDamageFormula(ItemStatistics enemyStatistics) {
        ItemStatistics all = allStatistics();
        return runPrimaryDamageFormula(all, enemyStatistics);
    }

    public static Map.Entry<Double, Boolean> runPrimaryDamageFormula(ItemStatistics originStatistics, ItemStatistics enemyStatistics) {
        boolean isCrit = false;
        double critChance = originStatistics.get(ItemStatistic.CRIT_CHANCE);
        if (Math.random() <= (critChance / 100))
            isCrit = true;

        double baseDamage = originStatistics.get(ItemStatistic.DAMAGE);
        double strength = originStatistics.get(ItemStatistic.STRENGTH);
        double critDamage = originStatistics.get(ItemStatistic.CRIT_DAMAGE);

        double initialDamage = (5 + baseDamage);
        double strengthDamage = (1 + (strength / 100));
        double criticalDamage = isCrit ? (1 + (critDamage / 100)) : 1;

        double baseDamageAdditiveMultiplier = 1 + (originStatistics.get(ItemStatistic.DAMAGE_ADDITIVE) / 100);
        double baseDamageMultiplicativeMultiplier = 1 + (originStatistics.get(ItemStatistic.DAMAGE_MULTIPLICATIVE));

        double damage = initialDamage * strengthDamage * criticalDamage * baseDamageAdditiveMultiplier * baseDamageMultiplicativeMultiplier;
        if (enemyStatistics.get(ItemStatistic.DEFENSE) > 0)
            damage = damage * (1 - (enemyStatistics.get(ItemStatistic.DEFENSE) / (enemyStatistics.get(ItemStatistic.DEFENSE) + 100)));
        return new AbstractMap.SimpleEntry<>(damage, isCrit);
    }

    public void boostManaRegeneration(double percent, int ticks) {
        manaRegenerationPercentBonus += percent;

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            manaRegenerationPercentBonus -= percent;
        }, TaskSchedule.tick(ticks), TaskSchedule.stop());
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
        });
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
        });
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
                MissionData.ActiveMission activeMission = activeMissions.get(0);
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
        });
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
        });
    }
}


