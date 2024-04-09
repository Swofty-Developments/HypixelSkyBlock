package net.swofty.types.generic.item.items.combat.slayer.wolf.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class EdibleMace implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_MUTTON, 9));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.GOLDEN_TOOTH, 18));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " A ",
                " B ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.EDIBLE_MACE), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 125D)
                .with(ItemStatistic.STRENGTH, 25D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: ME SMASH HEAD §e§lRIGHT CLICK",
                "§7Your next attack deals §cdouble",
                "§cdamage §7and weakens animals,",
                "§7making them deal §c-35% §7damage",
                "§7for §a30 §7seconds.",
                "§7§8Debuff doesn\u0027t stack.",
                "§8Mana Cost: §3100"));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
