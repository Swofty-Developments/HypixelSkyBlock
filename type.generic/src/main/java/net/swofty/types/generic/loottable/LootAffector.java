package net.swofty.types.generic.loottable;

import lombok.Getter;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.impl.EnchantmentLuck;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;

import java.util.Map;
import java.util.function.BiFunction;

@Getter
public enum LootAffector {
    MAGIC_FIND((player, original) -> {
        return original + (original * player.getStatistics().allStatistics().getOverall(ItemStatistic.MAGIC_FIND) * 0.01);
    }),
    PET_LUCK((player, original) -> {
        return original + (original * player.getStatistics().allStatistics().getOverall(ItemStatistic.PET_LUCK) * 0.01);
    }),
    ENCHANTMENT_LUCK((player, original) -> {
        Map<Integer, Integer> enchants = player.getStatistics().getAllOfEnchants(EnchantmentType.LUCK, true);
        int total = 0;
        for (int level : enchants.keySet()) {
            total += EnchantmentLuck.MULTIPLIERS[level - 1] * enchants.get(level);
        }
        return original + (original * total * 0.01);
    })
    ;

    // Player, Original Percentage, New Percentage
    private final BiFunction<SkyBlockPlayer, Double, Double> affector;

    LootAffector(BiFunction<SkyBlockPlayer, Double, Double> affector) {
        this.affector = affector;
    }
}
