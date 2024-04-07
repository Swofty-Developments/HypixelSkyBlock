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

public class FlawlessTopaz implements GemstoneImpl, Unstackable, DefaultCraftable {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d10964f3c479ad7d9afaf68a42cab7c107d2d884f575cae2f070ec6f935b3be";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWLESS;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.TOPAZ;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FLAWLESS_TOPAZ_GEM), 1)
                .add(ItemType.FINE_TOPAZ_GEM, 16)
                .add(ItemType.FINE_TOPAZ_GEM, 16)
                .add(ItemType.FINE_TOPAZ_GEM, 16)
                .add(ItemType.FINE_TOPAZ_GEM, 16)
                .add(ItemType.FINE_TOPAZ_GEM, 16);
    }
}
