package net.swofty.types.generic.item.items.combat.slayer.spider.craftable;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.ItemQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.*;

public class RecluseFang implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.TARANTULA_WEB, 4));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.SPIDER_CATALYST, 1));
        ingredientMap.put('C', new ItemQuantifiable(ItemTypeLinker.SPIDER_SWORD, 1));
        List<String> pattern = List.of(
                "AAA",
                "ABA",
                "ACA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER, new SkyBlockItem(ItemTypeLinker.RECLUSE_FANG), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 120D)
                .withBase(ItemStatistic.STRENGTH, 30D)
                .withBase(ItemStatistic.CRIT_DAMAGE, 20D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Squash \u0027em",
                "§7Squash Spiders to accumulate",
                "§7strength against them.",
                "§7Bonus: §c+0❁",
                "§8+1 strength per 40 squashed"));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
