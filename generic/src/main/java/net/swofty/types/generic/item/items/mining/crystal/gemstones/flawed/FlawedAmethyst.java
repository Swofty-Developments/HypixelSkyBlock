package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawedAmethyst implements GemstoneImpl, Sellable, DefaultCraftable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }

    @Override
    public double getSellValue() {
        return 240;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "71db59260895578d37e59505880602de940b088e5fff8da3e65201d739c86e84";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FLAWED_AMETHYST_GEM), 1)
                .add(ItemType.ROUGH_AMETHYST_GEM, 16)
                .add(ItemType.ROUGH_AMETHYST_GEM, 16)
                .add(ItemType.ROUGH_AMETHYST_GEM, 16)
                .add(ItemType.ROUGH_AMETHYST_GEM, 16)
                .add(ItemType.ROUGH_AMETHYST_GEM, 16);
    }
}
