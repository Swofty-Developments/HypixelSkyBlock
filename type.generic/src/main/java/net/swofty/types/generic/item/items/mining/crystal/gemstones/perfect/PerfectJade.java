package net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class PerfectJade implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3fced7977382bf71d4ee17ff5b919e0eb7972083c4cccfa175c8753ae40ba006";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.PERFECT;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.JADE;
    }
}
