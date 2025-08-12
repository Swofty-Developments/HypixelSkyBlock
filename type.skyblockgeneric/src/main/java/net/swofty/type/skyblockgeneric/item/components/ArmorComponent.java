package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.generic.item.SkyBlockItemComponent;

public class ArmorComponent extends SkyBlockItemComponent {
    public ArmorComponent() {
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
