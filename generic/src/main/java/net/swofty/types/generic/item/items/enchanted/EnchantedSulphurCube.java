package net.swofty.types.generic.item.items.enchanted;


import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Enchanted;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class EnchantedSulphurCube implements Enchanted, Sellable, Craftable {
    @Override
    public double getSellValue() {
        return 256000;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType.MINING, ItemType.ENCHANTED_SULPHUR);
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "4f0c28dfcf42d4dbd27c3378f67176c84800bc69b4cd531702c95ca6fb990458";
    }
}