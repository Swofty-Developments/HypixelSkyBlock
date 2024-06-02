package net.swofty.types.generic.item.items.mining.crystal.gemstones.rough;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class RoughAmber implements GemstoneImpl, Sellable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.ROUGH;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMBER;
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "da19436f6151a7b66d65ed7624add4325cfbbc2eee815fcf76f4c29ddf08f75b";
    }
}
