package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.item.SkyBlockItem;

public interface AnvilCombinable extends ExtraUnderNameDisplay {
    public void apply(SkyBlockItem item);

    default String getExtraUnderNameDisplay() {
        return "Combinable in Anvil";
    }
}
