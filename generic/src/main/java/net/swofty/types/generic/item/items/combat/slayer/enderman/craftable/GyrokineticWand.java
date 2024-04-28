package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class GyrokineticWand implements CustomSkyBlockItem, DefaultCraftable, Enchanted, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.NULL_OVOID, 16));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.ENCHANTED_EYE_OF_ENDER, 30));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.ENCHANTED_BLAZE_ROD, 15));
        List<String> pattern = List.of(
                "ABA",
                "ACA",
                "ACA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH, new SkyBlockItem(ItemType.GYROKINETIC_WAND), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Gravity Storm  §e§lLEFT CLICK",
                "§7Create a large §5rift §7at the",
                "§7aimed location, pulling all mobs",
                "§7together.",
                "§7§8Regen mana 10x slower for 3s",
                "§8after cast.",
                "§7§8Soulflow Cost: §310",
                "§8Mana Cost: §31,200",
                "§8Cooldown: §a30s",
                "",
                "§6Ability: Cells Alignment  §e§lRIGHT CLICK",
                "§7Apply §aAligned §7to yourself",
                "§7for §a6s§7, plus 4 nearby",
                "§7players on grouped islands.",
                "§7§8(Catacombs, etc.)",
                "§7",
                "§7§a||| Aligned",
                "§7Splits incoming damage and",
                "§7applies it over §a3s§7.",
                "§7§8Soulflow Cost: §32",
                "§8Mana Cost: §3220",
                "§8Cooldown: §a10s"));
    }
}
