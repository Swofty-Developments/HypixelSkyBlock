package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawedJade implements GemstoneImpl, Sellable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.JADE;
    }

    @Override
    public double getSellValue() {
        return 240;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "82282c6bb8343e0f0d61ee0747dada75344f332e9ff0acaa3adcdf09321d3dd";
    }
}
