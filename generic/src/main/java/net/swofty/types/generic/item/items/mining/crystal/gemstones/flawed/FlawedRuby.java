package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawedRuby implements GemstoneImpl, Sellable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.RUBY;
    }

    @Override
    public double getSellValue() {
        return 240;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "46d81068cbdf4a364231a26453d6cd660a0095f9cd8795307c5be667427712e";
    }
}
