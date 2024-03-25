package net.swofty.types.generic.item.impl;

public interface PetItem extends ExtraRarityDisplay, ExtraUnderNameDisplay {
    default String getExtraRarityDisplay() {
        return " PET ITEM";
    }

    default String getExtraUnderNameDisplay() {
        return "Consumed on use";
    }
}
