package net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class PerfectJasper implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "263f991b8e038e46b8ed7632f44ca2e30c15f42977070a8c8d8728e3fc04fc7c";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.PERFECT;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.JASPER;
    }
}
