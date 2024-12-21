package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;

import java.util.List;

@Getter
public class TieredTalismanComponent extends AccessoryComponent {
    private final ItemType baseTier;
    private final int tier;

    public TieredTalismanComponent(ItemType baseTier, int tier) {
        this.baseTier = baseTier;
        this.tier = tier;
    }
}