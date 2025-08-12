package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.generic.item.SkyBlockItemComponent;

public class PetItemComponent extends SkyBlockItemComponent {
    public PetItemComponent() {
        addInheritedComponent(new ExtraRarityComponent("PET ITEM"));
        addInheritedComponent(new ExtraUnderNameComponent("Consumed on use"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}