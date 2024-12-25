package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;

public class ArrowComponent extends SkyBlockItemComponent {
    public ArrowComponent() {
        addInheritedComponent(new ExtraRarityComponent("ARROW"));
    }
}
