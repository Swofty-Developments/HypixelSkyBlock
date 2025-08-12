package net.swofty.type.skyblockgeneric.enchantment.abstr;

import net.minestom.server.entity.LivingEntity;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.type.generic.user.HypixelPlayer;

public interface EventBasedEnchant {
    default ItemStatistics getStatisticsOnDamage(HypixelPlayer causer, LivingEntity receiver, int level) {
        return ItemStatistics.empty();
    }
}
