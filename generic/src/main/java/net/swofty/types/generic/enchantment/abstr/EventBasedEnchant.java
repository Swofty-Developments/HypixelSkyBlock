package net.swofty.types.generic.enchantment.abstr;

import net.minestom.server.entity.LivingEntity;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import javax.swing.text.html.parser.Entity;

public interface EventBasedEnchant {
    default ItemStatistics getStatisticsOnDamage(SkyBlockPlayer causer, LivingEntity receiver, int level) {
        return ItemStatistics.empty();
    }
}
