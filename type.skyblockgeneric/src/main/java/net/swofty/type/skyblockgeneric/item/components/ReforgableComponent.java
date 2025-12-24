package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

@Getter
public class ReforgableComponent extends SkyBlockItemComponent {
    private final ReforgeType reforgeType;

    public ReforgableComponent(ReforgeType reforgeType) {
        this.reforgeType = reforgeType;
    }
}