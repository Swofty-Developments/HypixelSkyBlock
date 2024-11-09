package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItemComponent;

@Getter
public class ReforgableComponent extends SkyBlockItemComponent {
    private final ReforgeType reforgeType;

    public ReforgableComponent(ReforgeType reforgeType) {
        this.reforgeType = reforgeType;
    }
}