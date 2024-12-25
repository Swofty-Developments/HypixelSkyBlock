package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.SkyBlockItemComponent;

@Getter
public class QuiverDisplayComponent extends SkyBlockItemComponent {
    public boolean shouldBeArrow;

    public QuiverDisplayComponent(boolean shouldBeArrow) {
        this.shouldBeArrow = shouldBeArrow;
    }
}
