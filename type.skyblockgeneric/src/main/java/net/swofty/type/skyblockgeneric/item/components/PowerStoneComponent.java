package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;

public class PowerStoneComponent extends SkyBlockItemComponent {
    public PowerStoneComponent() {
        addInheritedComponent(new ExtraRarityComponent("POWER STONE"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}