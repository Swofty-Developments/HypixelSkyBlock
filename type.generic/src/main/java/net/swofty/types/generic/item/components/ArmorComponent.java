package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;

public class ArmorComponent extends SkyBlockItemComponent {
    public ArmorComponent() {
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
