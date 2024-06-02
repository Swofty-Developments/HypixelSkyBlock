package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WardenHelmet implements CustomSkyBlockItem, SkullHead, DefaultCraftable, TrackedUniqueItem, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.REVIVED_HEART, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.WARDEN_HEART, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.ENCHANTED_IRON_BLOCK, 64));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "ABA",
                "C C");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemType.WARDEN_HELMET), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 300D)
                .withBase(ItemStatistic.DEFENSE, 100D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Brute Force",
                "§7Halves your §f✦ Speed §7but",
                "§7grants §c+20% §7base weapon",
                "§7damage for every §a25 §f✦",
                "§fSpeed§7."));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e5eb0bd85aaddf0d29ed082eac03fcade43d0ee803b0e8162add28a6379fb54e";
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
