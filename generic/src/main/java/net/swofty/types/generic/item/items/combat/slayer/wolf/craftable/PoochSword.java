package net.swofty.types.generic.item.items.combat.slayer.wolf.craftable;

import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class PoochSword implements CustomSkyBlockItem, Craftable, Reforgable, ExtraRarityDisplay, GemstoneItem {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.GOLDEN_TOOTH, 24));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.SHAMAN_SWORD, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " A ",
                " B ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.POOCH_SWORD), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 120D)
                .with(ItemStatistic.STRENGTH, 20D)
                .with(ItemStatistic.SPEED, 5D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deal §c+1 Damage §7per §c50 max ❤§7.",
                "§7Receive §a-20% §7damage from wolves.",
                "§7Gain §c+150❁ Strength §7against wolves."));
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.SWORDS;
    }

    @Override
    public String getExtraRarityDisplay() {
        return " SWORD";
    }

    @Override
    public Map<Gemstone.Slots, Integer> getGemstoneSlots() {
        return Map.of(
                Gemstone.Slots.JASPER, 50000 //+20 Fine Jasper
        );
    }
}
