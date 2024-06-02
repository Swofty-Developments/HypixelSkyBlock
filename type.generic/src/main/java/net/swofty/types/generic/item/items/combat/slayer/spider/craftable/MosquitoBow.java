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

public class MosquitoBow implements CustomSkyBlockItem, DefaultCraftable, BowImpl, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_IRON_INGOT, 64));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.TARANTULA_SILK, 64));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.DIGESTED_MOSQUITO, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " AB",
                "C B",
                " AB");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.TARANTULA_BROODFATHER, new SkyBlockItem(ItemType.MOSQUITO_BOW), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 251D)
                .withBase(ItemStatistic.STRENGTH, 151D)
                .withBase(ItemStatistic.CRIT_DAMAGE, 39D)
                .withBase(ItemStatistic.VITALITY, 20D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Nasty Bite",
                "§8Fully charged shots while sneaking",
                "§7Costs §b11% §7of max mana.",
                "§7Deal §c+19% §7damage.",
                "§7Heal for §a2x §7the mana cost."));
    }

    @Override
    public boolean shouldBeArrow() {
        return false;
    }

    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {}
}
