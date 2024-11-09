package net.swofty.types.generic.item.components;

import lombok.Getter;
import net.swofty.types.generic.item.crafting.SkyBlockRecipe;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.utility.RecipeParser;

import java.util.List;
import java.util.Map;

@Getter
public class CraftableComponent extends SkyBlockItemComponent {
    private final List<? extends SkyBlockRecipe<?>> recipes;

    public CraftableComponent(List<Map<String, Object>> recipeConfigs) {
        this.recipes = recipeConfigs.stream()
                .map(RecipeParser::parseRecipe)
                .toList();
    }

    public CraftableComponent(SkyBlockRecipe<?> recipe) {
        this.recipes = List.of(recipe);
    }

    public CraftableComponent(SkyBlockRecipe<?>... recipes) {
        this.recipes = List.of(recipes);
    }
}