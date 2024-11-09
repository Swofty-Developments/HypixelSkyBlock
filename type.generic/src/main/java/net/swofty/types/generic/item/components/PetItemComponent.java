package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;

public class PetItemComponent extends SkyBlockItemComponent {
    public PetItemComponent() {
        addInheritedComponent(new ExtraRarityComponent("PET ITEM"));
        addInheritedComponent(new ExtraUnderNameComponent("Consumed on use"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}