package net.swofty.types.generic.item.impl;

import java.util.List;

public interface DefaultCraftable {
    SkyBlockRecipe<?> getRecipe();

    default List<SkyBlockRecipe<?>> getRecipes() {
        return List.of(getRecipe());
    }
}
