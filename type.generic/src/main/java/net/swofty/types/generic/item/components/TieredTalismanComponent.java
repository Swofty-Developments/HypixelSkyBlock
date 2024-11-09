package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ItemType;

import java.util.List;

@Getter
public class TieredTalismanComponent extends TalismanComponent {
    private final ItemType baseTier;
    private final int tier;

    public TieredTalismanComponent(List<String> talismanDisplay, ItemType baseTier, int tier) {
        super(talismanDisplay);
        this.baseTier = baseTier;
        this.tier = tier;
    }
}