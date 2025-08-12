package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class ArrowComponent extends SkyBlockItemComponent {
    public ArrowComponent() {
        addInheritedComponent(new ExtraRarityComponent("ARROW"));
    }
}
