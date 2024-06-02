package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
public enum MinionRecipe {
    ONE((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.baseMaterial(), 10));
        map.put('B', new MaterialQuantifiable(materials.firstBaseItem(), 1));
        return map;
    }),
    TWO((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.baseMaterial(), 20));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    THREE((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.baseMaterial(), 40));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    FOUR((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.baseMaterial(), 64));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    FIVE((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 1));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    SIX((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 2));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    SEVEN((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 4));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    EIGHT((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 8));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    NINE((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 16));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    TEN((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 32));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    ELEVEN((materials) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.enchantedMaterial(), 64));
        map.put('B', new MaterialQuantifiable(materials.minionItem(), 1));
        return map;
    }),
    ;

    private final Function<MinionRecipeData, Map> recipeFunction;

    MinionRecipe(Function<MinionRecipeData, Map> recipeFunction) {
        this.recipeFunction = recipeFunction;
    }

    public static MinionRecipe fromNumber(int number) {
        try {
            return values()[number];
        } catch (ArrayIndexOutOfBoundsException e) {
            return ONE;
        }
    }

    public record MinionRecipeData(ItemType baseMaterial, ItemType enchantedMaterial, ItemType firstBaseItem, ItemType minionItem) {}
}
