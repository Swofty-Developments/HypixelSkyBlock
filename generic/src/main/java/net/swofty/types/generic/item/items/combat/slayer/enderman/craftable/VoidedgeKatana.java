package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

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

public class VoidedgeKatana implements CustomSkyBlockItem, Craftable, Reforgable, ExtraRarityDisplay, GemstoneItem {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.REFINED_MITHRIL, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.NULL_OVOID, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.VOIDWALKER_KATANA, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " B ",
                " C ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.VOIDEDGE_KATANA), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 125D)
                .with(ItemStatistic.STRENGTH, 60D)
                .with(ItemStatistic.CRIT_DAMAGE, 20D)
                .with(ItemStatistic.INTELLIGENCE, 50D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deal §a+200% §7damage to Endermen§7.",
                "§7§7Receive §a6% §7less damage",
                "§7from Endermen§7 when held.",
                "",
                "§6Ability: Soulcry §e§lRIGHT CLICK",
                "§7Gain §c+200⫽ Ferocity §7against",
                "§7Endermen for §a4s",
                "§7§8Soulflow Cost: §31",
                "§8Mana Cost: §3200",
                "§8Cooldown: §a4s"));
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
                Gemstone.Slots.JASPER, 50000, //+20 Fine Jasper
                Gemstone.Slots.SAPPHIRE, 100000 //+40 Fine Sapphire
        );
    }
}
