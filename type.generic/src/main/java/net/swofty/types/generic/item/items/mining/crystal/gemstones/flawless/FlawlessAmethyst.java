package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawlessAmethyst implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "d3623521c8111ad29e9dcf7acc56085a9ab07da732d1518976aee61d0b3e3bd6";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWLESS;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMETHYST;
    }

}
