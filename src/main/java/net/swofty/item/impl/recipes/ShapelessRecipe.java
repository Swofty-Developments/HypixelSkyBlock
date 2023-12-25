package net.swofty.item.impl.recipes;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemStack;
import net.swofty.item.ItemType;
import net.swofty.item.MaterialQuantifiable;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.SkyBlockRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShapelessRecipe extends SkyBlockRecipe<ShapelessRecipe> {
    public static final List<ShapelessRecipe> CACHED_RECIPES = new ArrayList<>();

    private final List<MaterialQuantifiable> ingredientList;

    protected ShapelessRecipe(SkyBlockItem result) {
        super(result);

        this.ingredientList = new ArrayList<>();
    }

    @Override
    public ShapelessRecipe setResult(SkyBlockItem result) {
        this.result = result;
        return this;
    }

    @Override
    public void init() {
        CACHED_RECIPES.add(this);
    }

    public ShapelessRecipe add(ItemType material, int amount) {
        return add(new MaterialQuantifiable(material, amount));
    }

    public ShapelessRecipe add(MaterialQuantifiable material) {
        ingredientList.add(material.clone());
        return this;
    }

    public static ShapelessRecipe parseShapelessRecipe(ItemStack[] stacks) {
        List<MaterialQuantifiable> materials = Arrays.stream(stacks)
                .map(MaterialQuantifiable::of)
                .toList();

        return CACHED_RECIPES.stream()
                .filter(recipe -> {
                    List<MaterialQuantifiable> ingredients = new ArrayList<>(recipe.getIngredientList());
                    for (MaterialQuantifiable material : materials) {
                        if (!ingredients.remove(material)) {
                            return false;
                        }
                    }
                    return ingredients.isEmpty();
                })
                .findFirst()
                .orElse(null);
    }
}
