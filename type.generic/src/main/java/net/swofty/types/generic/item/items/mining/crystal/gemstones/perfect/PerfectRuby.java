package net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class PerfectRuby implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "39b6e047d3b2bca85e8cc49e5480f9774d8a0eafe6dfa9559530590283715142";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.PERFECT;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.RUBY;
    }
}
