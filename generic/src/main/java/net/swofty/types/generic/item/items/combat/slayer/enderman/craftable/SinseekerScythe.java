package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class SinseekerScythe implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.NULL_ATOM, 4));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.NULL_OVOID, 8));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.SINFUL_DICE, 1));
        List<String> pattern = List.of(
                "ABA",
                "BCB",
                "ABA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH, new SkyBlockItem(ItemType.SINSEEKER_SCYTHE), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 100D)
                .withBase(ItemStatistic.STRENGTH, 100D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Sinrecall Transmission §e§lRIGHT CLICK",
                "§7Zap a line §54 §7blocks forward.",
                "§7",
                "§7Recast within 1s (§b1.5x mana§7)",
                "§7or warp back to starting point.",
                "§7",
                "§7Mobs crossing the line(s)",
                "§7receive a §cmelee hit §7from",
                "§7what you're holding.",
                "§8Mana Cost: §3260"));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}
