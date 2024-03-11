package net.swofty.types.generic.item.items.mining.crystal.gemstones.fine;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FineJasper implements GemstoneImpl, Sellable, Craftable {

    @Override
    public double getSellValue() {
        return 19200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "aac15f6fcf2ce963ef4ca71f1a8685adb97eb769e1d11194cbbd2e964a88978c";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FINE;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.JASPER;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FINE_JASPER_GEM), 1)
                .add(ItemType.FLAWED_JASPER_GEM, 16)
                .add(ItemType.FLAWED_JASPER_GEM, 16)
                .add(ItemType.FLAWED_JASPER_GEM, 16)
                .add(ItemType.FLAWED_JASPER_GEM, 16)
                .add(ItemType.FLAWED_JASPER_GEM, 16);
    }
}
