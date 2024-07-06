package net.swofty.types.generic.item.items.accessories.dungeon.scarf;

import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScarfsGrimoire implements TieredTalisman, NotFinishedYet, DefaultCraftable {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gain dungeon class experience",
                "§a+6% faster.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "bafb195cc75f31b619a077b7853653254ac18f220dc32d1412982ff437b4d57a";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.SCARFS_THESIS, 1));
        List<String> pattern = List.of(
                "AA",
                "AA"
        );
        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.SCARFS_GRIMOIRE), ingredientMap, pattern);
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.SCARFS_STUDIES;
    }

    @Override
    public Integer getTier() {
        return 3;
    }
}
