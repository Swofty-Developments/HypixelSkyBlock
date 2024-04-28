package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class SoulEsoward implements CustomSkyBlockItem, DefaultCraftable, Enchanted, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_BIRCH_WOOD, 18));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.SUMMONING_EYE, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.NULL_OVOID, 4));
        ingredientMap.put('D', new MaterialQuantifiable(ItemType.NULL_OVOID, 6));
        ingredientMap.put('E', new MaterialQuantifiable(ItemType.ENCHANTED_BIRCH_WOOD, 14));
        ingredientMap.put('F', new MaterialQuantifiable(ItemType.ENCHANTED_BIRCH_WOOD, 5));
        ingredientMap.put('G', new MaterialQuantifiable(ItemType.NULL_OVOID, 1));
        ingredientMap.put('H', new MaterialQuantifiable(ItemType.ENCHANTED_BIRCH_WOOD, 11));
        ingredientMap.put('I', new MaterialQuantifiable(ItemType.NULL_OVOID, 2));
        List<String> pattern = List.of(
                "ABC",
                "DEF",
                "GHI");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH, new SkyBlockItem(ItemType.SOUL_ESOWARD), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Soulward §e§lRIGHT CLICK",
                "§7Become §9invulnerable§7 for",
                "§7§a5s§7, but can't deal damage.",
                "§7Halves your damage for §a2s",
                "§a§7afterwards.",
                "§7§8Soulflow Cost: §310",
                "§8Mana Cost: §3350",
                "§8Cooldown: §a20s"));
    }
}
