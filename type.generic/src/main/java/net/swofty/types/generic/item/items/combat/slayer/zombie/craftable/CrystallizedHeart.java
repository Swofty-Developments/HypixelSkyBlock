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

public class CrystallizedHeart implements CustomSkyBlockItem, SkullHead, TrackedUniqueItem, StandardItem,
        DefaultCraftable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 50D)
                .withBase(ItemStatistic.DEFENSE, 10D)
                .withBase(ItemStatistic.INTELLIGENCE, 50D)
                .build();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "87dfb7c1ee4de31f54931eac5c657c145e4fa7fa09e3f52b1788a682b65ac75";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Healing Boost",
                "§7Doubles your §a☄ Mending §7and",
                "§7§4♨ Vitality §7while wearing.",
                "",
                "§2This armor piece is undead ༕!"));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_DIAMOND, 32));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.ZOMBIE_HEART, 1));
        List<String> pattern = List.of(
                "AAA",
                "ABA",
                "AAA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemType.CRYSTALLIZED_HEART), ingredientMap, pattern);
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}