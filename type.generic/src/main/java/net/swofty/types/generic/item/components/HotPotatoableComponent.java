package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.commons.item.PotatoType;
import net.swofty.types.generic.item.SkyBlockItemComponent;

@Getter
public class HotPotatoableComponent extends SkyBlockItemComponent {
    private final PotatoType potatoType;

    public HotPotatoableComponent(PotatoType type) {
        this.potatoType = type;
    }
}
