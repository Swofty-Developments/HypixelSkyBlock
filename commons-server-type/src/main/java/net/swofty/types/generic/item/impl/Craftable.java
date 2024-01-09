package net.swofty.types.generic.item.impl;

import java.util.List;

public interface Craftable {
    SkyBlockRecipe<?> getRecipe();

    default List<SkyBlockRecipe<?>> getRecipes() {
        return List.of(getRecipe());
    }
}
