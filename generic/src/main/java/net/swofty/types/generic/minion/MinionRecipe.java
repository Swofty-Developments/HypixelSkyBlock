package net.swofty.types.generic.minion;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Getter
public enum MinionRecipe {
    ONE((materials, originMaterial) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.getKey(), 10));
        map.put('B', new MaterialQuantifiable(originMaterial, 1));
        return map;
    }),
    TWO((materials, originMaterial) -> {
        Map<Character, MaterialQuantifiable> map = new HashMap<>();
        map.put('A', new MaterialQuantifiable(materials.getKey(), 20));
        map.put('B', new MaterialQuantifiable(originMaterial, 1));
        return map;
    }),
    ;

    private final BiFunction<Map.Entry<ItemType, ItemType>, ItemType, Map> recipeFunction;

    MinionRecipe(BiFunction<Map.Entry<ItemType, ItemType>, ItemType, Map> recipeFunction) {
        this.recipeFunction = recipeFunction;
    }

    public static MinionRecipe fromNumber(int number) {
        try {
            return values()[number];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
