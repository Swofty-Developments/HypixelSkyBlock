package net.swofty.types.generic.item.items.mining.crystal.gemstones.rough;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class RoughSapphire implements GemstoneImpl, Sellable {

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.ROUGH;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.SAPPHIRE;
    }

    @Override
    public double getSellValue() {
        return 3;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "cfcebe54dbc345ea7e22206f703e6b33befbe95b6a918bd1754b76188bc65bb5";
    }
}
