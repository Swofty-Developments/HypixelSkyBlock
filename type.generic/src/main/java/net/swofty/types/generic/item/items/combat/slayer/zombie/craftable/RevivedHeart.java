package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

import net.swofty.types.generic.gems.Gemstone;
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

public class RevivedHeart implements CustomSkyBlockItem, SkullHead, TrackedUniqueItem, DefaultCraftable, StandardItem, GemstoneItem, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 100D)
                .withBase(ItemStatistic.DEFENSE, 35D)
                .withBase(ItemStatistic.INTELLIGENCE, 100D)
                .build();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "4a61b4f8b070e4bc30a86b2290db6e57e2681c44e0250d1906a89adb8fc455b1";
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
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ZOMBIE_HEART, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.CRYSTALLIZED_HEART, 1));
        List<String> pattern = List.of(
                "AAA",
                "ABA",
                "AAA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemType.REVIVED_HEART), ingredientMap, pattern);
    }

    @Override
    public List<GemstoneItemSlot> getGemstoneSlots() {
        return List.of(
                new GemstoneItemSlot(Gemstone.Slots.RUBY, 0)
        );
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}