package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public interface KillEventPetAbility {
    void onPlayerKilledMob(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob);
}
