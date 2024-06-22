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

public class ScarfsThesis implements Talisman, NotFinishedYet, DefaultCraftable {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gain dungeon class experience",
                "§a+4% faster.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8ce4c87eb4dde27459e3e7f85921e7e57b11199260caa5ce63f139ee3d188c";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.SCARFS_STUDIES, 1));
        List<String> pattern = List.of(
                "AA",
                "AA"
        );
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.SCARFS_THESIS), ingredientMap, pattern);
    }
}
