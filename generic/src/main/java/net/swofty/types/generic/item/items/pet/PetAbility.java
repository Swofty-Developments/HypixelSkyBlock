package net.swofty.types.generic.item.items.pet;

import net.swofty.types.generic.item.SkyBlockItem;

import java.util.List;

public interface PetAbility {
    String getName();

    List<String> getDescription(SkyBlockItem instance);
}