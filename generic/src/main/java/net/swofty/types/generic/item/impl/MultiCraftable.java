package net.swofty.types.generic.item.impl;

import java.util.List;

public interface MultiCraftable extends Craftable {
    List<SkyBlockRecipe<?>> getRecipes();

    default SkyBlockRecipe<?> getRecipe() {
        return null;
    }
}
