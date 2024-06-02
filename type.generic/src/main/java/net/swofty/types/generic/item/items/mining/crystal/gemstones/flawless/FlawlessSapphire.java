package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawless;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawlessSapphire implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "957cfa9c75ba584645ee2af6d9867d767ddea4667cdfc72dc1061dd1975ca7d0";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWLESS;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.SAPPHIRE;
    }

}
