package net.swofty.types.generic.item.items.combat.slayer.wolf.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class WeirdTuba implements CustomSkyBlockItem, DefaultCraftable, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_IRON_INGOT, 20));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.GOLDEN_TOOTH, 20));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                "A A ",
                "ABA",
                " A ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SVEN_PACKMASTER, new SkyBlockItem(ItemType.WEIRD_TUBA), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Howl §e§lRIGHT CLICK",
                "§7You and 4 nearby players gain:",
                "§7§f+30✦ Speed",
                "§7§c+30❁ Strength",
                "§7§7for §a20 §7seconds.",
                "§7§8Effect doesn\u0027t stack.",
                "§8Mana Cost: §3150",
                "§8Cooldown: §a20s"));
    }

}
