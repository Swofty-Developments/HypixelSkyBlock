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
import net.swofty.commons.item.ItemType;

public class ScorpionBow implements CustomSkyBlockItem, DefaultCraftable, BowImpl, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.ENCHANTED_EMERALD, 32));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.TARANTULA_SILK, 16));
        ingredientMap.put('C', new ItemQuantifiable(ItemTypeLinker.ENCHANTED_GOLD_INGOT, 32));
        ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " AB",
                "C B",
                " AB");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER, new SkyBlockItem(ItemTypeLinker.SCORPION_BOW), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 110D)
                .withBase(ItemStatistic.STRENGTH, 10D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Stinger",
                "§8Fully charged shots while sneaking",
                "§7Slows the victim and deal §c35x",
                "§c§7your §cStrength ❁ §7as damage",
                "§7per second for §a6s§7.",
                "§8Mana Cost: §3150"));
    }

    @Override
    public boolean shouldBeArrow() {
        return false;
    }

    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {

    }
}
