package net.swofty.type.skyblockgeneric.enchantment.abstr;

import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public interface KillEventEnchant {
    default void onMobKilled(SkyBlockPlayer player, SkyBlockMob killedMob, int level) {
    }
}
