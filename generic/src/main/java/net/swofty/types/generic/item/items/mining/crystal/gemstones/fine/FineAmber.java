package net.swofty.types.generic.item.items.mining.crystal.gemstones.fine;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FineAmber implements GemstoneImpl, Sellable {

    @Override
    public double getSellValue() {
        return 19200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "4b1cce22de19ed6727abc5e6c2d57864c871a44c956bbe2eb3960269b686b8b3";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FINE;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMBER;
    }
}
