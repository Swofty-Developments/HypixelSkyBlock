package net.swofty.types.generic.item.items.mining.crystal.gemstones.rough;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class RoughAmethyst implements GemstoneImpl, Sellable {
    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "e493c6f540c7001fed97b07f6b4c89128e3a7c37563aa223f0acca314f175515";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.ROUGH;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }
}
