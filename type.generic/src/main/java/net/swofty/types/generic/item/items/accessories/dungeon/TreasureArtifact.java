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

public class TreasureArtifact implements Talisman, NotFinishedYet, DefaultCraftable {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Grants §a+3% §7extra loot to end",
                "§7of dungeon chests.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e10f20a55b6e188ebe7578459b64a6fbd825067bc497b925ca43c2643d059025";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.TREASURE_RING, 1));
        List<String> pattern = List.of(
                "AAA",
                "AAA",
                "AAA"
        );
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.TREASURE_ARTIFACT), ingredientMap, pattern);
    }
}
