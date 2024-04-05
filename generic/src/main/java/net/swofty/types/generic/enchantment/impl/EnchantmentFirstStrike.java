package net.swofty.types.generic.enchantment.impl;

import net.minestom.server.entity.LivingEntity;
import net.swofty.types.generic.enchantment.abstr.Ench;
import net.swofty.types.generic.enchantment.abstr.EnchFromTable;
import net.swofty.types.generic.enchantment.abstr.EventBasedEnchant;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentFirstStrike implements Ench, EnchFromTable, EventBasedEnchant {

    public static final double[] MULTIPLIERS = new double[]{25, 50, 75, 100, 125};

    @Override
    public String getDescription(int level) {
        return "Increases melee damage dealt by §a" + MULTIPLIERS[level - 1] + "% §7for the first hit on a mob.";
    }

    @Override
    public ApplyLevels getLevelsToApply(SkyBlockPlayer player) {
        return new ApplyLevels(new HashMap<>(Map.of(
                3, 36,
                4, 48,
                5, 179
        )));
    }

    @Override
    public List<EnchantItemGroups> getGroups() {
        return List.of(EnchantItemGroups.FISHING_WEAPON, EnchantItemGroups.LONG_SWORD,EnchantItemGroups.SWORD);
    }

    @Override
    public ItemStatistics getStatisticsOnDamage(SkyBlockPlayer causer, LivingEntity receiver, int level) {
        SkyBlockMob mob;
        if (receiver instanceof SkyBlockMob skyBlockMob) {
            mob = skyBlockMob;
        } else return ItemStatistics.EMPTY;

        if (mob.isHasBeenDamaged()) return ItemStatistics.EMPTY;

        mob.setHasBeenDamaged(true);
        return ItemStatistics.builder().with(ItemStatistic.DAMAGE_ADDITIVE, MULTIPLIERS[level - 1]).build();
    }

    @Override
    public ItemStatistics getStatistics(int level) {
        return ItemStatistics.EMPTY;
    }

    @Override
    public TableLevels getLevelsFromTableToApply(SkyBlockPlayer player) {
        return new TableLevels(new HashMap<>(Map.of(
                1, 20,
                2, 30,
                3, 40,
                4, 75
        )));
    }

    @Override
    public int getRequiredBookshelfPower() {
        return 5;
    }
}