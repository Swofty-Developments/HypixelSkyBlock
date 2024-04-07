package net.swofty.types.generic.item.impl;

import java.util.List;

public interface MultiDefaultCraftable extends DefaultCraftable {
    List<SkyBlockRecipe<?>> getRecipes();

    default SkyBlockRecipe<?> getRecipe() {
        return null;
    }
}
