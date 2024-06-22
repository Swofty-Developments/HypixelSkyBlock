package net.swofty.types.generic.item.items.accessories.dungeon;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreasureRing implements Talisman, NotFinishedYet, DefaultCraftable {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Grants §a+2% §7extra loot to end",
                "§7of dungeon chests.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "6a1cc5525a217a399b5b86c32f0f22dd91378874b5f44d5a383e18bc0f3bc301";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.TREASURE_TALISMAN, 1));
        List<String> pattern = List.of(
                "AAA",
                "A A",
                "AAA"
        );
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.TREASURE_RING), ingredientMap, pattern);
    }
}
