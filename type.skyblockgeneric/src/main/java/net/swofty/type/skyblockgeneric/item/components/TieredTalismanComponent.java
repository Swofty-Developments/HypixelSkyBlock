package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;

@Getter
public class TieredTalismanComponent extends AccessoryComponent {
    private final ItemType baseTier;
    private final int tier;

    public TieredTalismanComponent(ItemType baseTier, int tier) {
        this.baseTier = baseTier;
        this.tier = tier;
    }
}