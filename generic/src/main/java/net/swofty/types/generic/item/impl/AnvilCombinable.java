package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface AnvilCombinable extends ExtraUnderNameDisplay {
    void apply(SkyBlockItem item);
    boolean canApply(SkyBlockPlayer player, SkyBlockItem item);

    default String getExtraUnderNameDisplay() {
        return "Combinable in Anvil";
    }
}
