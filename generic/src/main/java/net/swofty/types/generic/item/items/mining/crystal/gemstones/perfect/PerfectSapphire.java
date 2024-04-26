package net.swofty.types.generic.item.items.mining.crystal.gemstones.perfect;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.TrackedUniqueItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class PerfectSapphire implements GemstoneImpl, TrackedUniqueItem {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8e93ebacb60b71793355fde0d4bba43a9c5ec09c3f38897c48c1f857523a0a29";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.PERFECT;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.SAPPHIRE;
    }
}
