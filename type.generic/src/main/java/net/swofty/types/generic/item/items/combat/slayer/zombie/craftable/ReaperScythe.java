package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

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

public class ReaperScythe implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.SCYTHE_BLADE, 1));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.REVENANT_VISCERA, 64));
        ingredientMap.put('C', new ItemQuantifiable(ItemTypeLinker.REAPER_FALCHION, 1));
        ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "ABC",
                "  C",
                "  C");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemTypeLinker.REAPER_SCYTHE), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 333D)
                .withBase(ItemStatistic.SPEED, 10D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Raise Souls §e§lRIGHT CLICK",
                "§7Monsters you kill using this",
                "§7item will drop their soul. You",
                "§7can click on their souls on the",
                "§7ground using this item to absorb",
                "§7them and then spawn them to",
                "§7fight by your side.",
                "§7",
                "§7Mana cost is based on the power",
                "§7of the monsters that you summon.",
                "§7Shift right-click to view and",
                "§7remove souls from this item. If",
                "§7your summoned monster dies the",
                "§7soul will be removed.",
                "§8Cooldown: §a1s",
                "",
                "§7Max Souls: §d3"));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
