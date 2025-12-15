package net.swofty.type.skyblockgeneric.enchantment.abstr;

import net.minestom.server.entity.LivingEntity;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public interface DamageEventEnchant {
    default void onDamageDealt(SkyBlockPlayer player, LivingEntity target, double damageDealt, int level) {
    }
}

