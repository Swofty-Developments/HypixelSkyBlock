package net.swofty.type.skyblockgeneric.item.handlers.pet;

import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public interface DamageEventPetAbility {
    void onPlayerDamagedByMob(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob, double damage);
}
