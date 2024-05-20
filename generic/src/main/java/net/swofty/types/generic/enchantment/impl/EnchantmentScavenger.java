package net.swofty.types.generic.enchantment.impl;

import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.user.PlayerEnchantmentHandler;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.MathUtility;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentScavenger implements Ench, EnchFromTable, SkyBlockEventClass {
    @Override
    public String getDescription(int level) {
        return "§7Scavenges §6+" + MathUtility.formatDecimals(0.3 + ((level - 1) * 0.3)) + " Coins §7per monster level on kill.";
    }

    @Override
    public ApplyLevels getLevelsToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 9,
                2, 18,
                3, 27,
                4, 36,
                5, 45
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.SCAVENGER_DISCOUNT)) {
            // Discount 25%
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new ApplyLevels(levels);
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.SWORD, EnchantItemGroups.FISHING_WEAPON, EnchantItemGroups.LONG_SWORD, EnchantItemGroups.GAUNTLET);
    }

    @Override
    public TableLevels getLevelsFromTableToApply(@NotNull SkyBlockPlayer player) {
        HashMap<Integer, Integer> levels = new HashMap<>(Map.of(
                1, 10,
                2, 20,
                3, 30
        ));

        if (player.hasCustomCollectionAward(CustomCollectionAward.SCAVENGER_DISCOUNT)) {
            // Discount 25%
            levels.replaceAll((k, v) -> (int) (v * 0.75));
        }

        return new TableLevels(levels);
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 3;
    }


    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        PlayerEnchantmentHandler enchantmentHandler = event.getPlayer().getEnchantmentHandler();

        PlayerEnchantmentHandler.EnchantmentHandlerResponse response = enchantmentHandler.getItemWithHighestLevelOf(
                EnchantmentType.SCAVENGER,
                PlayerEnchantmentHandler.EnchantedItemSource.HAND,
                PlayerEnchantmentHandler.EnchantedItemSource.ARMOR,
                PlayerEnchantmentHandler.EnchantedItemSource.ACCESSORY
        );

        if (response == null) return;
        SkyBlockMob mob = event.getKilledMob();
        int mobLevel = mob.getLevel();

        int enchantmentLevel = response.level();
        int coins = (int) (0.3 + ((enchantmentLevel - 1) * 0.3));

        event.getPlayer().setCoins(event.getPlayer().getCoins() + (coins * mobLevel));
        event.getPlayer().setDisplayReplacement(StatisticDisplayReplacement.builder()
                .display(String.valueOf((coins * mobLevel)))
                .purpose(StatisticDisplayReplacement.Purpose.ENCHANTMENT)
                .ticksToLast(20)
                .build(), StatisticDisplayReplacement.DisplayType.COINS);
    }
}
