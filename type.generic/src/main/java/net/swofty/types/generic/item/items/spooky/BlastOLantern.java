package net.swofty.types.generic.item.items.spooky;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlastOLantern implements CustomSkyBlockItem, SkullHead, Sellable, CustomDisplayName, DefaultCraftable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public double getSellValue() {
        return 530;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "68f2ffc6fb4e9959b9a7a317f51a6775a159ddc2241dbd6c774d3ac08b6";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Throwable explosive. Deals §a2",
                "§a§7damage to §6Spooky §7enemies",
                "§7and §c3,000 §7damage against",
                "§7non-scary enemies!"));
    }

    @Override
    public String getDisplayName(SkyBlockItem item) {
        return "Blast O'Lantern";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.GREEN_CANDY, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.PUMPKIN_GUTS, 1));
        List<String> pattern = List.of(
                "AAA",
                "ABA",
                "AAA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SPECIAL, new SkyBlockItem(ItemType.BLAST_O_LANTERN, 32), ingredientMap, pattern);
    }
}
