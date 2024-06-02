package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public class FlawedAmber implements GemstoneImpl, Sellable {
    @Override
    public double getSellValue() {
        return 240;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "173bcfc39eb85df1848535985214060a1bd1b3bb47defe4201476edc31671744";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.AMBER;
    }
}
