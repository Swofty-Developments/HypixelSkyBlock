package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.swofty.type.generic.item.SkyBlockItemComponent;

@Getter
public class QuiverDisplayComponent extends SkyBlockItemComponent {
    public boolean shouldBeArrow;

    public QuiverDisplayComponent(boolean shouldBeArrow) {
        this.shouldBeArrow = shouldBeArrow;
    }
}
