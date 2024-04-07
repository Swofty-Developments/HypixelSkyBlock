package net.swofty.types.generic.item.items.mining.crystal.gemstones.fine;

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

public class FineAmethyst implements GemstoneImpl, Sellable, DefaultCraftable {
    @Override
    public double getSellValue() {
        return 19200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7a1ee5ffce04eb7da592d42414ff35e1bf38194d6b82e310dbc6261b47fb9c91";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FINE;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FINE_AMETHYST_GEM), 1)
                .add(ItemType.FLAWED_AMETHYST_GEM, 16)
                .add(ItemType.FLAWED_AMETHYST_GEM, 16)
                .add(ItemType.FLAWED_AMETHYST_GEM, 16)
                .add(ItemType.FLAWED_AMETHYST_GEM, 16)
                .add(ItemType.FLAWED_AMETHYST_GEM, 16);
    }
}
