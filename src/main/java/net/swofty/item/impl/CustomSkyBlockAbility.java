package net.swofty.item.impl;

import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;

public interface CustomSkyBlockAbility {
    String getAbilityName();
    String getAbilityDescription();
    void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem);

    int getManaCost();

    int getAbilityCooldownTicks();

    AbilityActivation getAbilityActivation();
}
