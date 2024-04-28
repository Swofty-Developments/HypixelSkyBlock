package net.swofty.types.generic.item.items.combat.slayer.spider.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class ScorpionFoil implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.TARANTULA_SILK, 4));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.ENCHANTED_ACACIA_WOOD, 64));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.STICK, 1));
        List<String> pattern = List.of(
                "ABA",
                "ABA",
                "ACA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER, new SkyBlockItem(ItemType.SCORPION_FOIL), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 100D)
                .withBase(ItemStatistic.STRENGTH, 100D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deal §a250% §7damage against Spiders.",
                "",
                "§6Ability: Heartstopper",
                "§7You have §e4 Ⓞ tickers§7.",
                "§7Blocking clears §e1 Ⓞ §7and heals §c60❤§7.",
                "§7Once all tickers are cleared,",
                "§7your next attack is empowered",
                "§7for §c+250% damage§7.",
                "§8Tickers refill after 5 seconds."));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
