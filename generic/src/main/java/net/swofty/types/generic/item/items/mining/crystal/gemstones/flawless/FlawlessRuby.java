package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Craftable;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.Unstackable;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawlessRuby implements GemstoneImpl, Unstackable, Craftable {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "926a248fbbc06cf06e2c920eca1cac8a2c96164d3260494bed142d553026cc6";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWLESS;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.RUBY;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FLAWLESS_RUBY_GEM), 1)
                .add(ItemType.FINE_RUBY_GEM, 16)
                .add(ItemType.FINE_RUBY_GEM, 16)
                .add(ItemType.FINE_RUBY_GEM, 16)
                .add(ItemType.FINE_RUBY_GEM, 16)
                .add(ItemType.FINE_RUBY_GEM, 16);
    }
}
