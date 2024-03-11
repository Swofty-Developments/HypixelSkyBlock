package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class FlawedSapphire implements GemstoneImpl, Sellable, Craftable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.SAPPHIRE;
    }

    @Override
    public double getSellValue() {
        return 240;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8a0af99e8d8703194a847a55268cf5ef4ac4eb3b24c0ed86551339d10b646529";
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.MINING,
                new SkyBlockItem(ItemType.FLAWED_SAPPHIRE_GEM), 1)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16)
                .add(ItemType.ROUGH_SAPPHIRE_GEM, 16);
    }
}
