package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

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

public class VoidwalkerKatana implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.NULL_SPHERE, 16));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.STICK, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " A ",
                " B ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH, new SkyBlockItem(ItemTypeLinker.VOIDWALKER_KATANA), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 80D)
                .withBase(ItemStatistic.STRENGTH, 40D)
                .withBase(ItemStatistic.CRIT_DAMAGE, 10D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deal §a+150% §7damage to Endermen§7.",
                "§7§7Receive §a3% §7less damage",
                "§7from Endermen§7 when held."));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
