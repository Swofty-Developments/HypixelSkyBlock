package net.swofty.types.generic.item.items.mining.crystal.gemstones.flawed;

import net.swofty.types.generic.gems.GemRarity;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneImpl;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class FlawedSapphire implements GemstoneImpl, Sellable {
    @Override
    public double getSellValue() {
        return 240;
    }

    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8a0af99e8d8703194a847a55268cf5ef4ac4eb3b24c0ed86551339d10b646529";
    }

    @Override
    public GemRarity getAssociatedGemRarity() {
        return GemRarity.FLAWED;
    }

    @Override
    public Gemstone getAssociatedGemstone() {
        return Gemstone.SAPPHIRE;
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7A slightly better version of",
                "§7§bSapphire§7, but it could still",
                "§7use some work.",
                "",
                "§7§7Some say that when §eharnessed",
                "§eproperly§7, it can give its",
                "§7owner extra §b✎ Intelligence§7."));
    }
}
