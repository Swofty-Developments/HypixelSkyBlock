package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.gems.Gemstone;

import java.util.Map;

public interface GemstoneItem {
    Map<Gemstone.Slots, Integer> getGemstoneSlots();
}
