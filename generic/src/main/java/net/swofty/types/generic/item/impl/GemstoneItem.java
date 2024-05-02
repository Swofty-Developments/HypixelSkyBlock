package net.swofty.types.generic.item.impl;

import net.swofty.types.generic.gems.Gemstone;

import java.util.List;

public interface GemstoneItem {
    List<GemstoneItemSlot> getGemstoneSlots();

    class GemstoneItemSlot {
        public Gemstone.Slots slot;
        public Integer unlockPrice;

        public GemstoneItemSlot(Gemstone.Slots slot, Integer unlockPrice) {
            this.slot = slot;
            this.unlockPrice = unlockPrice;
        }
    }
}
