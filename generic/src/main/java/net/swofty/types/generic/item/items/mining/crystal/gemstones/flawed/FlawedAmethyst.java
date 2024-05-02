package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawedAmethyst implements GemstoneImpl, Sellable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }

    @Override
    public double getSellValue() {
        return 240;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "71db59260895578d37e59505880602de940b088e5fff8da3e65201d739c86e84";
    }
}
