package net.swofty.types.generic.item.items.mining.crystal.gemstones.fine;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FineJasper implements GemstoneImpl, Sellable {

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
}
