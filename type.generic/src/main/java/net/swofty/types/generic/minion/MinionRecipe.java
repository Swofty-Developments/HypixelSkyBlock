package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.commons.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public class MinionRecipe {
    private final Function<MinionRecipeData, Map> recipeFunction;

    MinionRecipe(Function<MinionRecipeData, Map> recipeFunction) {
        this.recipeFunction = recipeFunction;
    }

    public static MinionRecipe fromNumber(int number) {
        try {
            return new MinionRecipe((materials) -> {
                Map<Character, ItemQuantifiable> map = new HashMap<>();
                map.put('A', new ItemQuantifiable(materials.minionIngredients().get(number).getItem(), materials.minionIngredients().get(number).getAmount()));
                if (number != 0) {
                    ItemTypeLinker itemTypeLinker = materials.minionItem();
                    SkyBlockItem item = new SkyBlockItem(itemTypeLinker);
                    item.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(number, 1));
                    map.put('B', new ItemQuantifiable(item, 1));
                } else {
                    map.put('B', new ItemQuantifiable(materials.firstBaseItem(), 1));
                }
                return map;
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            return new MinionRecipe((materials) -> {
                Map<Character, ItemQuantifiable> map = new HashMap<>();
                map.put('A', new ItemQuantifiable(materials.minionIngredients().get(1).getItem(), materials.minionIngredients().get(1).getAmount()));
                map.put('B', new ItemQuantifiable(materials.firstBaseItem(), 1));
                return map;
            });
        }
    }

    public record MinionRecipeData(List<MinionIngredient> minionIngredients, ItemTypeLinker firstBaseItem, ItemTypeLinker minionItem) {}
}
