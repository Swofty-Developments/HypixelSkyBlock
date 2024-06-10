package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EtherwarpConduit implements CustomSkyBlockItem, DefaultCraftable, SkullHead, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.NULL_OVOID, 3));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.REFINED_TITANIUM, 16));
        List<String> pattern = List.of(
                "AAA",
                "ABA",
                "AAA");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.VOIDGLOOM_SERAPH, new SkyBlockItem(ItemTypeLinker.ETHERWARP_CONDUIT), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§6Ability: Ether Transmission §e§lRIGHT CLICK",
                "§7Teleport to your targetted block",
                "§7up to §a57 blocks §7away.",
                "§7§8Soulflow Cost: §31",
                "§8Mana Cost: §3180"));
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "ca70e0206f6166048441dfe08e053a6017d914f35c6b1fb0f558c50574f970d0";
    }
}
