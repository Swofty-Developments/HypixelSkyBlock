package net.swofty.types.generic.item.items.mining.crystal.gemstones.fine;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FineSapphire implements GemstoneImpl, Sellable {

    @Override
    public double getSellValue() {
        return 19200;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "36161daa3589ec9c8187459ac36fd4dd2646c040678d3bfacb72a2210c6c801c";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FINE;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.SAPPHIRE;
    }
}
