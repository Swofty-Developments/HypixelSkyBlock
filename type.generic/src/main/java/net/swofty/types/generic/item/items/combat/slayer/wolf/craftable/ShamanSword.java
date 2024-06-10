package net.swofty.types.generic.item.items.combat.slayer.wolf.craftable;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.*;
import net.swofty.commons.item.ItemType;

public class ShamanSword implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet{
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.GOLDEN_TOOTH, 2));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_IRON_INGOT, 8));
        ingredientMap.put('C', new MaterialQuantifiable(ItemTypeLinker.ENCHANTED_BONE, 4));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " B ",
                " C ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SVEN_PACKMASTER, new SkyBlockItem(ItemTypeLinker.SHAMAN_SWORD), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 100D)
                .withBase(ItemStatistic.STRENGTH, 20D)
                .withBase(ItemStatistic.SPEED, 5D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deal §c+1 Damage §7per §c50 max ❤§7.",
                "§7Receive §a-20% §7damage from wolves."));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
