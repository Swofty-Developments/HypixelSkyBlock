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

public class ReaperMask implements CustomSkyBlockItem, SkullHead, DefaultCraftable, GemstoneItem,
        TrackedUniqueItem, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.REVENANT_VISCERA, 32));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.BEHEADED_HORROR, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.ENCHANTED_STRING, 32));
        ingredientMap.put('D', new MaterialQuantifiable(ItemType.REVIVED_HEART, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "A A",
                "BCD",
                "A A");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemType.REAPER_MASK), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 150D)
                .withBase(ItemStatistic.DEFENSE, 100D)
                .withBase(ItemStatistic.INTELLIGENCE, 100D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Evil Incarnate",
                "§7While wearing:",
                "§2• §7Doubles your §a☄ Mending§7.",
                "§2• §7Doubles your §4♨ Vitality§7.",
                "§2• §7Zombie Armor triggers on all hits.",
                "§2• §7Store §b2 §7extra necromancer souls.",
                "§2• §7Summon §b2 §7more necromancy mobs.",
                "",
                "§2This armor piece is undead ༕!"));
    }

    @Override
    public List<GemstoneItemSlot> getGemstoneSlots() {
        return List.of(
                new GemstoneItemSlot(Gemstone.Slots.RUBY, 0),
                new GemstoneItemSlot(Gemstone.Slots.SAPPHIRE, 50000)
        );
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "1fc0184473fe882d2895ce7cbc8197bd40ff70bf10d3745de97b6c2a9c5fc78f";
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.HELMET;
    }
}
