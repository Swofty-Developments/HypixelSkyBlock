package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class AccessoryComponent extends SkyBlockItemComponent {
    public AccessoryComponent() {
        addInheritedComponent(new ExtraRarityComponent("ACCESSORY"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
