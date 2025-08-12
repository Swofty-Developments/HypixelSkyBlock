package net.swofty.type.skyblockgeneric.item.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.item.crafting.SkyBlockRecipe;
import net.swofty.type.generic.utility.RecipeParser;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CraftableComponent extends SkyBlockItemComponent {
    private final List<SkyBlockRecipe<?>> recipes;
    private boolean defaultCraftable;

    public CraftableComponent(List<Map<String, Object>> recipeConfigs) {
        List<SkyBlockRecipe<?>> parsedRecipes = new ArrayList<>();
        for (Map<String, Object> config : recipeConfigs) {
            try {
                parsedRecipes.add(RecipeParser.parseRecipe(config));
            } catch (Exception e) {
                Logger.error("Failed to parse recipe " + config.get("result"));
            }
        }
        this.recipes = parsedRecipes;
        this.defaultCraftable = true;
    }

    public CraftableComponent(boolean defaultCraftable, SkyBlockRecipe<?>... recipes) {
        this.recipes = List.of(recipes);
        this.defaultCraftable = defaultCraftable;
    }

    public CraftableComponent(SkyBlockRecipe<?>[] array, boolean defaultCraftable) {
        this.recipes = List.of(array);
        this.defaultCraftable = defaultCraftable;
    }

    public CraftableComponent(SkyBlockRecipe<?> recipe, boolean defaultCraftable) {
        this.recipes = List.of(recipe);
        this.defaultCraftable = defaultCraftable;
    }
}