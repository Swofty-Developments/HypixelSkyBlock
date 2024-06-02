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

import java.util.*;

public class AxeOfTheShreeded implements CustomSkyBlockItem, DefaultCraftable, StandardItem, GemstoneItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.REVENANT_VISCERA, 64));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.SHARD_OF_THE_SHREDDED, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.REAPER_FALCHION, 1));
        List<String> pattern = List.of(
                "ABA",
                "BCB",
                "ABA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemType.AXE_OF_THE_SHREDDED), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 140D)
                .withBase(ItemStatistic.STRENGTH, 115D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Heal §c50❤ §7per hit.",
                "§7Deal §a+250% §7damage to Zombies§7.",
                "§7§7Receive §a25% §7less damage",
                "§7from Zombies§7 when held.",
                "",
                "§6Ability: Throw §e§lRIGHT CLICK",
                "§7Throw your axe damaging all",
                "§7enemies in its path dealing",
                "§7§c10%§7 melee damage.",
                "§7Consecutive throws stack §c2x",
                "§c§7damage but cost §92x §7mana up",
                "§7to 16x",
                "§8Mana Cost: §320"));
    }

    @Override
    public List<GemstoneItemSlot> getGemstoneSlots() {
        return List.of(
                new GemstoneItemSlot(Gemstone.Slots.JASPER, 50000),
                new GemstoneItemSlot(Gemstone.Slots.COMBAT, 100000)
        );
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
