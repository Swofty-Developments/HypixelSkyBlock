package net.swofty.type.skyblockgeneric.item.handlers.pet;

import net.swofty.type.generic.item.SkyBlockItem;

import java.util.List;

public interface PetAbility {
    String getName();

    List<String> getDescription(SkyBlockItem instance);
}