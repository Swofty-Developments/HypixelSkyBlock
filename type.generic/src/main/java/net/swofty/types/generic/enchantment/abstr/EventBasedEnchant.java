package net.swofty.types.generic.enchantment.abstr;

import net.minestom.server.entity.LivingEntity;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface EventBasedEnchant {
    default ItemStatistics getStatisticsOnDamage(SkyBlockPlayer causer, LivingEntity receiver, int level) {
        return ItemStatistics.empty();
    }
}
