package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public interface FallDamageEventPetAbility {
    double onPlayerFallDamage(SkyBlockPlayer player, SkyBlockItem pet, double damage);
}
