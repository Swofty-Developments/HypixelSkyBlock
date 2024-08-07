package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

import net.swofty.types.generic.gems.Gemstone;
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

public class ReaperFalchion implements CustomSkyBlockItem, DefaultCraftable, StandardItem, GemstoneItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, ItemQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new ItemQuantifiable(ItemTypeLinker.REVIVED_HEART, 1));
        ingredientMap.put('B', new ItemQuantifiable(ItemTypeLinker.REVENANT_CATALYST, 1));
        ingredientMap.put('C', new ItemQuantifiable(ItemTypeLinker.REVENANT_FALCHION, 1));
        ingredientMap.put(' ', new ItemQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " B ",
                " C ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemTypeLinker.REAPER_FALCHION), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 120D)
                .withBase(ItemStatistic.STRENGTH, 100D)
                .withBase(ItemStatistic.INTELLIGENCE, 200D)
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
    public List<GemstoneItemSlot> getGemstoneSlots() {
        return List.of(
                new GemstoneItemSlot(Gemstone.Slots.JASPER, 50000)
        );
    }
    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
