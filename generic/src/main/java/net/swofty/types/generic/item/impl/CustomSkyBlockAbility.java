package net.swofty.types.generic.item.impl;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

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
