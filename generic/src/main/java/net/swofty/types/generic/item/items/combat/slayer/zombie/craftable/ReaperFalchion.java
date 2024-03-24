package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

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

public class ReaperFalchion implements CustomSkyBlockItem, Craftable, Reforgable, ExtraRarityDisplay, GemstoneItem {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.REVIVED_HEART, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.REVENANT_CATALYST, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.REVENANT_FALCHION, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " B ",
                " C ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.REAPER_FALCHION), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 120D)
                .with(ItemStatistic.STRENGTH, 100D)
                .with(ItemStatistic.INTELLIGENCE, 200D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Heal §c10❤ §7per hit.",
                "§7Deal §a+200% §7damage to Zombies§7.",
                "§7§7Receive §a20% §7less damage",
                "§7from Zombies§7 when held."));
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
