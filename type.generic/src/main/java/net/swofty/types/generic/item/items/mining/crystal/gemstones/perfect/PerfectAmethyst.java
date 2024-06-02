package net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class PerfectAmethyst implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d886e0f41185b18a3afd89488d2ee4caa0735009247cccf039ced6aed752ff1a";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.PERFECT;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }
}
