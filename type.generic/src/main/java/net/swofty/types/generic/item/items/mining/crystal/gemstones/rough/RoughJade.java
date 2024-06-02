package net.swofty.types.generic.item.items.mining.crystal.gemstones.rough;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class RoughJade implements GemstoneImpl, Sellable {
    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3b4c2afd544d0a6139e6ae8ef8f0bfc09a9fd837d0cad4f5cd0fe7f607b7d1a0";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.ROUGH;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.JADE;
    }
}
