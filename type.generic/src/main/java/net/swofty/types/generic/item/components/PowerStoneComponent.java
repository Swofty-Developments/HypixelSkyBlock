package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;

public class PowerStoneComponent extends SkyBlockItemComponent {
    public PowerStoneComponent() {
        addInheritedComponent(new ExtraRarityComponent("POWER STONE"));
    }
}