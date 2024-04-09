package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class RevenantFalchion implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.CRYSTALLIZED_HEART, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.UNDEAD_CATALYST, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.UNDEAD_SWORD, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " B ",
                " C ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.REAPER_FALCHION), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 90D)
                .with(ItemStatistic.STRENGTH, 50D)
                .with(ItemStatistic.INTELLIGENCE, 100D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deals §a+150% §7damage to",
                "§7§7Zombies§7."));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
