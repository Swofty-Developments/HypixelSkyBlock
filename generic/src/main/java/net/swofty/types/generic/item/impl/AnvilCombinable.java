package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public interface AnvilCombinable extends ExtraUnderNameDisplay {
    void apply(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem);
    boolean canApply(SkyBlockPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem);

    default int applyCostLevels(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player) { return 0; }

    default String getExtraUnderNameDisplay() {
        return "Combinable in Anvil";
    }
}
