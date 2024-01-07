package net.swofty.commons.skyblock.item.impl;

import lombok.Getter;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.item.SkyBlockItem;

public interface CustomSkyBlockAbility {
    String getAbilityName();

    String getAbilityDescription();

    void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem);

    int getManaCost();

    int getAbilityCooldownTicks();

    AbilityActivation getAbilityActivation();

    @Getter
    enum AbilityActivation {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        ;

        private final String display;

        AbilityActivation(String display) {
            this.display = display;
        }
    }
}
