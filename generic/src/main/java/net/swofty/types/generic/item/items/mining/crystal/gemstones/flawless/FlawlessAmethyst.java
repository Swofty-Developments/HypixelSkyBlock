package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawlessAmethyst implements GemstoneImpl, Unstackable, DefaultCraftable {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d3623521c8111ad29e9dcf7acc56085a9ab07da732d1518976aee61d0b3e3bd6";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWLESS;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FLAWLESS_AMETHYST_GEM), 1)
                .add(ItemType.FINE_AMETHYST_GEM, 16)
                .add(ItemType.FINE_AMETHYST_GEM, 16)
                .add(ItemType.FINE_AMETHYST_GEM, 16)
                .add(ItemType.FINE_AMETHYST_GEM, 16)
                .add(ItemType.FINE_AMETHYST_GEM, 16);
    }
}
