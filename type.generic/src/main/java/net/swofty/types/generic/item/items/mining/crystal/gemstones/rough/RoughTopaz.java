package net.swofty.types.generic.item.items.mining.crystal.gemstones.rough;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class RoughTopaz implements GemstoneImpl, Sellable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.ROUGH;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.TOPAZ;
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "3fd960722ec29c66716ae5ca97b9b6b2628984e1d6f9d2592cd089914206a1b";
    }
}
